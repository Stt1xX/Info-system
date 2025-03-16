package com.example.backend.rabbitMQ;


import com.example.shared_module.entities.User;
import com.example.shared_module.queue_managment.RequestWrapper;
import com.example.shared_module.queue_managment.ResponseWrapper;
import com.example.shared_module.utils.JsonUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileSender {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;

    public FileSender(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public ResponseEntity<?> send(MultipartFile file, User user) throws IOException {
        byte[] fileBytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        Object jsonResp;
        try {
            String jsonRequest = JsonUtil.toJson(new RequestWrapper(user, fileBytes, fileName));
            if (jsonRequest == null) {
                return new ResponseEntity<>("Error during distributed import", HttpStatus.BAD_REQUEST);
            }
            jsonResp = rabbitTemplate.convertSendAndReceive(queue.getName(), jsonRequest);
        } catch(AmqpException ex){
            return new ResponseEntity<>("RabbitMQ is not responding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (jsonResp == null) {
            return new ResponseEntity<>("No response from the consumer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseWrapper response = JsonUtil.fromJson((String)jsonResp, ResponseWrapper.class);
        if (response == null){
            return new ResponseEntity<>("Error during distributed import", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.message(), HttpStatusCode.valueOf(response.status()));
    }
}