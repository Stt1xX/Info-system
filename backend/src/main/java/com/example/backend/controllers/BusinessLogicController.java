package com.example.backend.controllers;

import com.example.backend.servicies.BusinessLogicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business_logic")
public class BusinessLogicController{

    private final BusinessLogicService businessLogicService;

    public BusinessLogicController(BusinessLogicService businessLogicService) {
        this.businessLogicService = businessLogicService;
    }

    @PostMapping("/longing_mood")
    public ResponseEntity<?> getBusinessLogic() {
        return businessLogicService.setLongingMood();
    }
}
