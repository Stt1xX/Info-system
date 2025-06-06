package com.example.backend.controllers;


import com.example.shared_module.entities.DTO.RegRequestDTO;
import com.example.shared_module.servicies.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AdminController {

    private final RequestService requestService;

    @Autowired
    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @MessageMapping("/admin/get_reg_requests")
    @SendTo("/topic/reg_requests")
    public List<RegRequestDTO> getRegRequests()
    {
        return requestService.getAllRequests();
    }

    @MessageMapping("/admin/accept")
    @SendTo("/topic/reg_requests")
    public List<RegRequestDTO> acceptRequest(@Payload Integer id){
        requestService.acceptRequestById(id);
        return requestService.getAllRequests();
    }

    @MessageMapping("/admin/reject")
    @SendTo("/topic/reg_requests")
    public List<RegRequestDTO> rejectRequest(@Payload Integer id){
        requestService.rejectRequestById(id);
        return requestService.getAllRequests();
    }
}
