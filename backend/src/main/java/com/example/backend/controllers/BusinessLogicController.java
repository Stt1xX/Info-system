package com.example.backend.controllers;


import com.example.shared_module.servicies.BusinessLogicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/transfer_heroes")
    public ResponseEntity<?> transferHeroes() {
        return businessLogicService.transferHeroes();
    }

    @GetMapping("/soundtrack_number_by_name/{soundtrackName}")
    public ResponseEntity<?> getSoundtrackNumber(@PathVariable String soundtrackName) {
        return businessLogicService.getSoundtrackNumber(soundtrackName);
    }

    @DeleteMapping("delete_person_by_impact_speed/{impactSpeed}")
    public ResponseEntity<?> deletePersonByImpactSpeed(@PathVariable Integer impactSpeed) {
        return businessLogicService.deletePersonByImpactSpeed(impactSpeed);
    }
}
