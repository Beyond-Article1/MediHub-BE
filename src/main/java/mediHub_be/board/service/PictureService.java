package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PictureService {

    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;
    private final FlagRepository flagRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.board.service.PictureService");    // Logger

    // 이미지 업로드 및 Picture 엔터티 생성
    @Transactional
    public List<String> uploadPictureWithFlag(String flagType, Long entitySeq, List<MultipartFile> pictures) {

        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq).orElse(null);
        try {
            return pictures.stream()
                    .map(image -> uploadPicture(image, flag))
                    .collect(Collectors.toList());
        } catch (Exception e) {
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
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean deletePictures(String FLAG_TYPE, long entitySeq) {
        try {
            List<Picture> pictureList = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(FLAG_TYPE, entitySeq);
            pictureList.forEach(picture -> {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                pictureRepository.delete(picture);
            });
            logger.info("{}의 사진 제거 성공", FLAG_TYPE);
        } catch (Exception e) {
            logger.warn("사진 제거 중 예기치 못한 에러가 발생했습니다.");
            return false;
        }

        return true;
    }
}
