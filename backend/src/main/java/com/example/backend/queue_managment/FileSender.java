package com.example.backend.queue_managment;

import com.example.backend.entities.User;
import com.example.backend.servicies.UserService;
import com.example.backend.utils.JsonUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSender {

    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;

    private final Queue queue;

    public FileSender(RabbitTemplate rabbitTemplate, UserService userService, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.queue = queue;
    }

    public ResponseEntity<?> send(MultipartFile file) throws Exception {
        byte[] fileBytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        User user = userService.getCurrentUser();
        Object jsonResp;
        try {
            jsonResp = rabbitTemplate.convertSendAndReceive(
                    queue.getName(),
                    JsonUtil.toJson(new RequestWrapper(user, fileBytes, fileName))
            );
        } catch(AmqpException ex){
            return new ResponseEntity<>("RabbitMQ is not responding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (jsonResp == null) {
            return new ResponseEntity<>("No response from the consumer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseWrapper response = JsonUtil.fromJson((String)jsonResp, ResponseWrapper.class);

        return new ResponseEntity<>(response.message(), HttpStatusCode.valueOf(response.status()));
    }
}