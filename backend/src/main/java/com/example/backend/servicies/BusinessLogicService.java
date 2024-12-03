package com.example.backend.servicies;

import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.enums.Mood;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusinessLogicService {

    private final HumanService humanService;

    public BusinessLogicService(HumanService humanService) {
        this.humanService = humanService;
    }

    public ResponseEntity<?> setLongingMood() {
        int n = humanService.getAll()
                .stream()
                .map(human -> {
                    human.setMood(Mood.LONGING);
                    ResponseEntity<?> resp = humanService.update(human.getId(), new HumanDTO(human));
                    return resp.getStatusCode() == org.springframework.http.HttpStatus.OK ? 1 : 0;
                })
                .reduce(0, Integer::sum);
        System.out.println(n);
        return ResponseEntity.ok("Business logic");
    }
}
