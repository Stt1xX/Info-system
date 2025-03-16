package com.example.consumer.queue_managment;


import com.example.shared_module.queue_managment.RequestWrapper;
import com.example.shared_module.queue_managment.ResponseWrapper;
import com.example.shared_module.servicies.FileService;
import com.example.shared_module.utils.JsonUtil;
import org.hibernate.StaleObjectStateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Component
public class FileConsumer {

    private final FileService fileService;

    public FileConsumer(FileService fileService) {
        this.fileService = fileService;
    }

    @RabbitListener(queues = "imported_files", containerFactory = "rabbitListenerContainerFactory")
    public String consume(String requestJson) throws Exception {
        RequestWrapper request = JsonUtil.fromJson(requestJson, RequestWrapper.class);
        if (request.user() == null) {
            return JsonUtil.toJson(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(), "User can't be null"));
        }
        ResponseEntity<?> response;
        try {
            response = fileService.localImport(request.file(), request.fileName(), request.user());
        } catch (OptimisticLockingFailureException | StaleObjectStateException e){
            response = ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Entity was modified or deleted by another transaction.");
        } catch (DataIntegrityViolationException e){
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MaxUploadSizeExceededException e){
            response = new ResponseEntity<>("File is too large", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            response = new ResponseEntity<>("Invalid input, please try again", HttpStatus.BAD_REQUEST);
        }
        return JsonUtil.toJson(new ResponseWrapper(response.getStatusCode().value(), (String) response.getBody()));
    }
}
