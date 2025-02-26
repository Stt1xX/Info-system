package com.example.backend.controllers;

import com.example.backend.servicies.MinioService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try (InputStream stream = minioService.downloadFile(fileName)) {
            byte[] fileBytes = stream.readAllBytes();
            //set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

