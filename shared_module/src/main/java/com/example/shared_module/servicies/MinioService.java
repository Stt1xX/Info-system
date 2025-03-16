package com.example.backend.servicies;
import com.example.backend.utils.Utils;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final UserService userService;
    private final ImportRecordService importRecordService;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket-name}") String bucket,
            UserService userService, ImportRecordService importRecordService1
    ) {
        this.userService = userService;
        this.importRecordService = importRecordService1;
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucket;
    }

    public String uploadFile(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            String fileName = Utils.generateFileName(userService.getCurrentUser().getUsername(), "document");
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.length(), 5242880)
                            .contentType(Files.probeContentType(file.toPath()))
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new DataIntegrityViolationException("Error uploading file to Minio");
        }
    }



    public InputStream downloadFile(Integer recordId) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(importRecordService.findById(recordId).getFileName())
                        .build()
        );
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}
