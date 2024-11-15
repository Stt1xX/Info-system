package com.example.backend.controllers;

import com.example.backend.entities.DTO.RequestDTO;
import com.example.backend.servicies.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
//@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final RequestService requestService;

    @Autowired
    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @MessageMapping("/get_reg_requests")
    @SendTo("/topic/reg_requests")
    public List<RequestDTO> getRegRequests()
    {
        return requestService.getAllRequests();
    }

    @MessageMapping("/accept")
    @SendTo("/topic/reg_requests")
    public List<RequestDTO> acceptRequest(@Payload Integer id){
        requestService.acceptRequestById(id);
        return requestService.getAllRequests();
    }

    @MessageMapping("/reject")
    @SendTo("/topic/reg_requests")
    public List<RequestDTO> rejectRequest(@Payload Integer id){
        requestService.rejectRequestById(id);
        return requestService.getAllRequests();
    }
}
