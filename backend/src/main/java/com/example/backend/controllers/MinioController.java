package com.example.backend.controllers;

import com.example.shared_module.servicies.MinioService;
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

    @GetMapping("/download/{recordId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer recordId) {
        try (InputStream stream = minioService.downloadFile(recordId)) {
            byte[] fileBytes = stream.readAllBytes();
            //set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(recordId + ".xlsx").build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("I'm sorry I can't do that".getBytes());
        }
    }

}

