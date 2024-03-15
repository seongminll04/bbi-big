package bbibig.bbibig.global.s3service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    String serverImgUpload(MultipartFile serverImg) throws IOException;
    String uploadToS3(String fileName, byte[] fileBytes, String contentType);
    String getFileExtension(String fileName);
    void removeFile(String fileName);
}