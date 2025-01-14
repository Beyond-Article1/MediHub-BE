package mediHub_be.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;
    private final FlagService flagService;

    @Transactional
    public String replaceBase64WithUrls(String content, String flagType, Long entitySeq) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode contentNode = objectMapper.readTree(content);

            Flag flag = flagService.findFlag(flagType, entitySeq).orElse(null);
            log.info("flag 정보:"+ flag);

            // Base64 이미지 처리
            List<String> uploadedUrls = new ArrayList<>();
            for (JsonNode block : contentNode.get("blocks")) {
                if ("image".equals(block.get("type").asText())) {
                    JsonNode fileNode = block.get("data").get("file");
                    String base64Url = fileNode.get("url").asText();
                    log.info("베이스이미지" + base64Url);
                    if (base64Url.startsWith("data:image")) {
                        String uploadedUrl = uploadBase64Image(base64Url, flag);
                        log.info("업로드 url" + uploadedUrl);
                        uploadedUrls.add(uploadedUrl);

                        // JSON 노드 URL 교체
                        ((ObjectNode) fileNode).put("url", uploadedUrl);
                    }
                }
            }

            // 트랜잭션 롤백 시 S3에서 삭제 처리
            registerRollbackHandler(uploadedUrls);

            return objectMapper.writeValueAsString(contentNode);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_UPLOAD_ERROR);
        }
    }

    private String uploadBase64Image(String base64Data, Flag flag) {
        try {
            // Base64 데이터를 디코딩
            String[] parts = base64Data.split(",");
            String base64Image = parts[1];
            String fileExtension = parts[0].split(";")[0].split("/")[1];

            System.out.println("File Extension: " + fileExtension);

            byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Image);
            String fileName = "uploaded_image_" + System.currentTimeMillis() + "." + fileExtension;

            // S3 업로드
            AmazonS3Service.MetaData metaData = amazonS3Service.uploadFromByteArray(decodedBytes, fileName, fileExtension);

            // Picture 엔티티 저장
            Picture pic = Picture.builder()
                    .flag(flag)
                    .pictureName(fileName)
                    .pictureChangedName(metaData.getChangeFileName())
                    .pictureUrl(metaData.getUrl())
                    .pictureType(fileExtension)
                    .pictureIsDeleted(false)
                    .build();
            pictureRepository.save(pic);

            return metaData.getUrl();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void uploadPicture(MultipartFile picture, Flag flag) {
        try {
            //S3에 업로드
            AmazonS3Service.MetaData metaData = amazonS3Service.upload(picture);

            // Picture 엔티티 저장
            Picture pic = Picture.builder()
                    .flag(flag)
                    .pictureName(metaData.getOriginalFileName())
                    .pictureChangedName(metaData.getChangeFileName())
                    .pictureUrl(metaData.getUrl())
                    .pictureType(metaData.getType())
                    .pictureIsDeleted(false)
                    .build();
            pictureRepository.save(pic);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void deletePictures(Flag flag) {
        List<Picture> pictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(flag.getFlagType(), flag.getFlagEntitySeq());
        pictures.forEach(picture -> {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            pictureRepository.delete(picture);
        });
    }


    @Transactional
    public List<Picture> getPicturesByFlagTypeAndEntitySeqAndIsDeletedIsNotNull(String flagType, Long entitySeq) {
        return pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeqAndPictureIsDeletedIsNotNull(flagType, entitySeq);
    }

    @Transactional
    public List<String> getPicturesURLByFlagTypeAndEntitySeqAndIsDeletedIsNotNull(String flagType, Long entitySeq) {
        List<Picture> pictures =  pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeqAndPictureIsDeletedIsNotNull(flagType, entitySeq);
        List<String> urls = new ArrayList<>();
        for(Picture picture : pictures) {
            urls.add(picture.getPictureUrl());
        }
        return urls;
    }

    private void cleanupUploadedFiles(List<String> uploadedUrls) {
        uploadedUrls.forEach(url -> {
            try {
                amazonS3Service.deleteImageFromS3(url);
                log.info("S3 파일 삭제 성공: {}", url);
            } catch (Exception ex) {
                log.warn("S3 파일 삭제 실패: {}", url, ex);
            }
        });
    }

    private void registerRollbackHandler(List<String> uploadedUrls) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.info("트랜잭션 롤백 발생 - S3 업로드된 파일 삭제 시작");
                        cleanupUploadedFiles(uploadedUrls);
                    }
                }
            });
        }
    }
    @Transactional(readOnly = true)
    public String getCaseSharingFirstImageUrl(Long caseSharingSeq ){
        Flag flag = flagService.findFlag("CASE_SHARING", caseSharingSeq).orElse(null);

        if (flag != null) {
            Picture profile = pictureRepository.findFirstByFlag_FlagSeqOrderByCreatedAtDesc(flag.getFlagSeq()).orElse(null);
            if (profile != null) {
                return profile.getPictureUrl();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public String getUserProfileUrl(long userSeq) {

        log.info("프로필 사진 조회 요청이 생성되었습니다.");

        Flag flag = flagService.findFlag("USER", userSeq).orElse(null);

        if (flag != null) {
            Picture profile = pictureRepository.findFirstByFlag_FlagSeqOrderByCreatedAtDesc(flag.getFlagSeq()).orElse(null);

            if (profile != null) {
                log.info("프로필 사진 조회가 성공하였습니다.");
                return profile.getPictureUrl();
            } else {
                log.warn("프로필 사진이 없습니다.");
                return UserService.DEFAULT_PROFILE_URL;
            }
        } else {
            log.warn("프로필 사진이 없습니다.");
            return UserService.DEFAULT_PROFILE_URL;
        }
    }

    @Transactional
    public void deletePictures(String flagType, Long entitySeq) {
        flagService.findFlag(flagType, entitySeq).ifPresent(this::deletePictures);
    }
}
