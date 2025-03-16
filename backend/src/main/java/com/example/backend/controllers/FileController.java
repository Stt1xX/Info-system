package com.example.backend.controllers;


import com.example.backend.rabbitMQ.FileSender;
import com.example.shared_module.servicies.ImportRecordService;
import com.example.shared_module.servicies.FileService;
import com.example.shared_module.servicies.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/files")
public class FileController {

    private final FileSender fileSender;
    private final FileService fileService;
    private final UserService userService;
    private final ImportRecordService importRecordService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public FileController(FileSender fileSender, FileService fileService, UserService userService, ImportRecordService importRecordService, SimpMessagingTemplate simpMessagingTemplate) {
        this.fileSender = fileSender;
        this.fileService = fileService;
        this.userService = userService;
        this.importRecordService = importRecordService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/import/{distributionFlag}")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file, @PathVariable Integer distributionFlag) {
            ResponseEntity<?> resp;
            try {
                if (distributionFlag == 1)
                    resp = fileSender.send(file, userService.getCurrentUser());
                else
                    resp = fileService.localImport(file.getBytes(), file.getOriginalFilename(), userService.getCurrentUser());
                simpMessagingTemplate.convertAndSend("/topic/humans", "");
                simpMessagingTemplate.convertAndSend("/topic/cars", "");
                simpMessagingTemplate.convertAndSend("/topic/coordinates", "");
                simpMessagingTemplate.convertAndSend("/topic/audit", "");
                return resp;
            } catch (IOException e) {
                return ResponseEntity.badRequest().body("Error during file processing");
            }
    }

    @GetMapping("/history/{pageNumber}")
    public ResponseEntity<?> getHistory(@PathVariable Integer pageNumber) {
        return importRecordService.getHistory(pageNumber);
    }
}
