package bbibig.bbibig.global.s3service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class S3ServiceImpl implements S3Service{

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    private final AmazonS3Client amazonS3Client;


    /**
     * serverImg 업로드
     */
    @Override
    public String serverImgUpload (MultipartFile serverImg) throws IOException {

        // .png 같은 확장자 추출
        String fileExtension = getFileExtension(Objects.requireNonNull(serverImg.getOriginalFilename()));
        // 파일 저장 경로를 포함한 파일이름
        String fileName = "server/" + UUID.randomUUID() + fileExtension;

        return uploadToS3(fileName, serverImg.getBytes(), serverImg.getContentType());
    }

    /**
     * S3 업로드 메서드
     */
    @Override
    public String uploadToS3(String fileName, byte[] fileBytes, String contentType) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(contentType);
        objectMetaData.setContentLength(fileBytes.length);

        try {
            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, fileName, new ByteArrayInputStream(fileBytes), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String filePath = amazonS3Client.getUrl(S3Bucket, fileName).toString(); // 접근 가능한 URL 가져오기

            if (filePath == null) {
                throw new IllegalArgumentException("이미지 경로를 가져오지 못하였습니다.");
            }

            return filePath;
        } catch (AmazonClientException e) {
            throw new RuntimeException("S3에 이미지를 업로드하는데 실패했습니다.", e);
        }
    }

    /**
     * 파일 이름에서 확장자를 추출 메서드
     */
    @Override
    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex);
        }

        throw new IllegalArgumentException("해당 파일의 확장자를 확인할 수 없습니다.");
    }

    /**
     * S3 내의 파일 삭제 메서드
     * */
    @Override
    public void removeFile(String fileName) {
        amazonS3Client.deleteObject(
                new DeleteObjectRequest(S3Bucket, fileName)
        );
    }
}

