package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;
    private final FlagService flagService;

    // 이미지 업로드 및 Picture 엔터티 생성
    @Transactional
    public List<String> uploadPictureWithFlag(String flagType, Long entitySeq, List<MultipartFile> pictures) {
        Flag flag = flagService.findFlag(flagType, entitySeq).orElse(null);
        List<String> uploadedUrls = new ArrayList<>(); // 업로드된 S3 파일 URL 추적

        try {
            // S3 업로드 및 URL 저장
            List<String> urls = pictures.stream()
                    .map(image -> {
                        String url = uploadPicture(image, flag);
                        uploadedUrls.add(url); // 업로드된 URL 추가
                        return url;
                    })
                    .collect(Collectors.toList());

            // 트랜잭션 롤백 시 S3에서 업로드된 파일 삭제
            registerRollbackHandler(uploadedUrls);

            return urls;
        } catch (Exception e) {
            // S3 업로드 중 에러 발생 시 업로드된 파일 삭제
            cleanupUploadedFiles(uploadedUrls);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_UPLOAD_ERROR);
        }
    }

    // 본문 내 placeholder -> S3 url로 치환
    @Transactional
    public String replacePlaceHolderWithUrls(String content, List<MultipartFile> images, String flagType, Long entitySeq) {
        List<String> urls = uploadPictureWithFlag(flagType, entitySeq, images);

        // 태그와 URL 매핑
        for (int i = 0; i < urls.size(); i++) {
            String placeholder = "[img-" + (i + 1) + "]"; // `[img-1]`, `[img-2]`, ...
            content = content.replace(placeholder, "<img src='" + urls.get(i) + "' />");
        }

        return content;
    }

    @Transactional
    public String uploadPicture(MultipartFile picture, Flag flag) {
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

            return metaData.getUrl();
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
    public String getUserProfileUrl(long userSeq) {

        Picture profile = pictureRepository.findUserProfile(userSeq).orElse(null);

        if (profile != null) {
            return profile.getPictureUrl();
        } else {
            return UserService.DEFAULT_PROFILE_URL;
        }
    }

    @Transactional
    public void deletePictures(String flagType, Long entitySeq) {
        flagService.findFlag(flagType, entitySeq).ifPresent(this::deletePictures);
    }
}
