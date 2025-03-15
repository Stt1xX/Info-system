package com.example.backend.controllers;

import com.example.backend.queue_managment.FileSender;
import com.example.backend.servicies.FileService;
import com.example.backend.servicies.ImportRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final ImportRecordService importRecordService;
    private final FileService fileService;

    public FileController(ImportRecordService importRecordService, FileService fileService) {
        this.importRecordService = importRecordService;
        this.fileService = fileService;
    }

    @PostMapping("/import/{distributionFlag}")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file, @PathVariable Integer distributionFlag) {
        return fileService.mainImport(file, distributionFlag);
    }

    @GetMapping("/history/{pageNumber}")
    public ResponseEntity<?> getHistory(@PathVariable Integer pageNumber) {
        return importRecordService.getHistory(pageNumber);
    }
}
