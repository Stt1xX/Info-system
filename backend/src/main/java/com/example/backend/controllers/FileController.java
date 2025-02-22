package com.example.backend.controllers;

import com.example.backend.servicies.FileService;
import com.example.backend.servicies.ImportRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final ImportRecordService importRecordService;

    public FileController(FileService fileService, ImportRecordService importRecordService) {
        this.fileService = fileService;
        this.importRecordService = importRecordService;
    }

    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        return fileService.mainImport(file);
    }

    @GetMapping("/history/{pageNumber}")
    public ResponseEntity<?> getHistory(@PathVariable Integer pageNumber) {
        return importRecordService.getHistory(pageNumber);
    }
}
