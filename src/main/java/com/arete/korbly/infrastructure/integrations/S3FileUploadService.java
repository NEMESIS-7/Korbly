package com.arete.korbly.infrastructure.integrations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.arete.korbly.modules.shared.enums.UploadFileResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class S3FileUploadService {
    Dotenv dotenv = Dotenv.configure().load();
    private final AmazonS3 amazonS3;
    private final String bucketName = "korbly";

    public S3FileUploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public UploadFileResponse uploadFile(String key, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(key.toString());
        metadata.setContentLength(file.getSize());

        System.out.println("file metadata: " + metadata);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
        PutObjectResult result = amazonS3.putObject(putObjectRequest);
        return new UploadFileResponse(
                file.getOriginalFilename(),
                key.toString(),
                file.getContentType(),
                result.getETag(),
                file.getSize(),
                result.getVersionId()
        );
    }

    public String uploadFile(String key, byte[] file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);

        amazonS3.putObject(bucketName, key, inputStream, metadata);

        return key;
    }
}
