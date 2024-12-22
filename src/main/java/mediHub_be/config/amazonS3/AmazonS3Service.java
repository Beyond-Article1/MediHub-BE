package mediHub_be.config.amazonS3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private static final Logger log = LoggerFactory.getLogger(AmazonS3Service.class);

    private final AmazonS3Client amazonS3Client;

    @Value("${s3.bucket}")
    private String amazonS3Bucket;

    public MetaData upload(MultipartFile image) throws IOException {

        MetaData metaData = new MetaData();

        // 업로드 할 파일의 원래 이름 가져오기
        String originalFileName = image.getOriginalFilename();

        metaData.setOriginalFileName(originalFileName);

        // 파일 이름 변경
        String fileName = changeFileName(originalFileName);

        // 변경된 파일 이름 메타 데이터에 저장
        metaData.setChangeFileName(fileName);

        // S3에 업로드 할 파일의 메타 데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(image.getContentType());
        metaData.setType(image.getContentType());
        metadata.setContentLength(image.getSize());
        metaData.setUrl(amazonS3Client.getUrl(amazonS3Bucket, fileName).toString());

        // S3에 파일 업로드
        amazonS3Client.putObject(amazonS3Bucket, fileName, image.getInputStream(), metadata);

        // 기존 파일 이름과 변경된 파일 이름 로그 출력
        log.info("origin name = " + metaData.getOriginalFileName());
        log.info("changed name = " + metaData.getChangeFileName());
        log.info("url = " + metaData.getUrl());

        // 업로드 한 파일의 S3 URL 주소 반환
        return metaData;
    }

    // 사진 url을 이용하여 S3에서 해당 사진을 제거
    // getKeyFromImageAddress()를 호출하여 삭제에 필요한 key 추출
    public void deleteImageFromS3(String imageAddress) {

        String key = getKeyFromImageAddress(imageAddress);

        try {

            amazonS3Client.deleteObject(new DeleteObjectRequest(amazonS3Bucket, key));
        } catch(Exception e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_DELETE_ERROR);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){

        try {

            URL url = new URL(imageAddress);

            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");

            // 맨 앞 '/' 제거
            return decodingKey.substring(1);
        } catch(MalformedURLException | UnsupportedEncodingException e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_DELETE_ERROR);
        }
    }

    private String changeFileName(String originalFileName) {

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 타임 스탬프와 UUID를 이용한 고유 파일 이름
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        return newFileName;
    }

    public MetaData uploadFromByteArray(byte[] imageBytes, String fileName, String contentType) {

        MetaData metaData = new MetaData();

        // 파일 이름 변경
        String changedFileName = changeFileName(fileName);

        metaData.setOriginalFileName(fileName);
        metaData.setChangeFileName(changedFileName);

        // S3에 업로드할 파일의 메타 데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(imageBytes.length);

        try {
            // S3에 파일 업로드
            amazonS3Client.putObject(amazonS3Bucket, changedFileName, new java.io.ByteArrayInputStream(imageBytes), metadata);

            // 업로드된 파일의 URL 생성
            String fileUrl = amazonS3Client.getUrl(amazonS3Bucket, changedFileName).toString();
            metaData.setUrl(fileUrl);
            metaData.setType(contentType);

            // 로그 출력
            log.info("origin name = " + metaData.getOriginalFileName());
            log.info("changed name = " + metaData.getChangeFileName());
            log.info("url = " + metaData.getUrl());

            return metaData;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_UPLOAD_ERROR);
        }
    }



    @Getter
    @Setter
    public static class MetaData {

        private String originalFileName;
        private String changeFileName;
        private String url;
        private String type;
    }
}