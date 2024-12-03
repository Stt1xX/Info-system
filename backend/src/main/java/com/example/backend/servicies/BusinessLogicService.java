package com.example.backend.servicies;

import com.example.backend.entities.Car;

import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.entities.enums.Mood;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BusinessLogicService {

    private final HumanService humanService;
    private final AuditService auditService;
    private final CarService carService;
    private final Checker checker;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    public BusinessLogicService(HumanService humanService, AuditService auditService, CarService carService, Checker checker, SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.humanService = humanService;
        this.auditService = auditService;
        this.carService = carService;
        this.checker = checker;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    public ResponseEntity<?> setLongingMood() {
        int n = humanService.getAll()
                .stream()
                .map(human -> {
                    if (Objects.equals(human.getMood(), Mood.LONGING.getDisplayName())) {
                        return 0;
                    }
                    human.setMood(Mood.LONGING);
                    ResponseEntity<?> resp = checker.check_rights(human);
                    if (resp.getStatusCode() != HttpStatus.OK) {
                        return 0;
                    }
                    humanService.save(human);
                    auditService.doCommit(human.getId(), EntityType.HUMAN, "Update");
                    return 1;
                })
                .reduce(0, Integer::sum);
        simpMessagingTemplate.convertAndSend("/topic/humans", "");
        return n == 0 ? ResponseEntity.ok("No heroes found") :
                ResponseEntity.ok("Mood has been set to the saddest possible one for " + n + " characters");
    }

    public ResponseEntity<?> transferHeroes(){
        Car car = carService.findByName("Lada Kalina");
        if (car == null) {
            car = new Car();
            car.setCool(false);
            car.setName("Lada Kalina");
            car.setAuthor(userService.getCurrentUser().getUsername());
            car = carService.save(car);
            simpMessagingTemplate.convertAndSend("/topic/cars", "");
            auditService.doCommit(car.getId(), EntityType.CAR, "Create");
        }
        Car finalCar = car;
        int n = humanService.getAll()
                .stream()
                .map(human -> {
                    if (Objects.equals(human.getCar().getName(), "Lada Kalina")) {
                        return 0;
                    }
                    human.setCar(finalCar);
                    ResponseEntity<?> resp = checker.check_rights(human);
                    if (resp.getStatusCode() != HttpStatus.OK) {
                        return 0;
                    }
                    humanService.save(human);
                    auditService.doCommit(human.getId(), EntityType.HUMAN, "Update");
                    return 1;
                })
                .reduce(0, Integer::sum);
        simpMessagingTemplate.convertAndSend("/topic/humans", "");
        return n == 0 ? ResponseEntity.ok("No heroes found") :
                ResponseEntity.ok("Heroes have been transferred to the red Lada Kalina for " + n + " characters");
    }

    public ResponseEntity<?> getSoundtrackNumber(String soundtrackName) {
        return ResponseEntity.ok("The number of objects whose Soundtrack name field value is less than the specified one is " +
                humanService.getAll()
                .stream()
                .map(human -> human.getSoundtrackName().compareTo(soundtrackName) < 0 ? 1 : 0)
                .reduce(0, Integer::sum));

    }

    public ResponseEntity<?> deletePersonByImpactSpeed(Integer impactSpeed) {
        List<Human> human = humanService.findAllByImpactSpeed(impactSpeed);
        for (Human h : human) {
            ResponseEntity<?> resp = humanService.delete(h.getId());
            if (resp.getStatusCode() == HttpStatus.OK) {
                auditService.deleteCommits(h.getId(), EntityType.HUMAN);
                return resp;
            }
        }
        return ResponseEntity.ok("No heroes found");
    }
}
