package com.example.consumer.queue_managment;

import com.example.consumer.services.FileService;
import com.example.consumer.services.UserGlobalService;
import com.example.consumer.utils.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FileConsumer {

    private final FileService fileService;

    public FileConsumer(FileService fileService) {
        this.fileService = fileService;
    }

    @RabbitListener(queues = "imported_files", containerFactory = "rabbitListenerContainerFactory")
    public String consume(String requestJson) throws Exception {
        RequestWrapper request = JsonUtil.fromJson(requestJson, RequestWrapper.class);
        UserGlobalService.setCurrentUser(request.user());
        if (request.user() == null) {
            return JsonUtil.toJson(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(), "User can't be null"));
        }
        ResponseEntity<?> response = fileService.mainImport(request.file(), request.fileName());
        return JsonUtil.toJson(new ResponseWrapper(response.getStatusCode().value(), (String) response.getBody()));
    }
}
