package com.example.consumer.services;

import com.example.consumer.entities.Car;
import com.example.consumer.entities.Coordinates;
import com.example.consumer.entities.DTO.CarDTO;
import com.example.consumer.entities.DTO.CoordinatesDTO;
import com.example.consumer.entities.DTO.HumanDTO;
import com.example.consumer.entities.Human;
import com.example.consumer.entities.enums.EntityType;
import com.example.consumer.repositories.HumanRepository;
import com.example.consumer.services.enums.CounterIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HumanService extends ItemService<HumanDTO, Human> {

    private final CarService carService;
    private final CoordinatesService coordinatesService;

    @Autowired
    public HumanService(HumanRepository humanRepository, AuditService auditService, Checker checker,
                        CarService carService, CoordinatesService coordinatesService) {
        super(humanRepository, auditService, checker, Human.class);
        this.carService = carService;
        this.coordinatesService = coordinatesService;
    }

    @Override
    public ResponseEntity<?> add(HumanDTO humanDTO) {
        return ResponseEntity.ok("");
    }

    @Override
    public ResponseEntity<?> addAll(Map<Integer, HumanDTO> humanDTOs) {
        int[] ret_arr = new int[]{0, 0, 0};
        Set<Human> humans = new HashSet<>(getAll());
        List<Human> newHumans = new ArrayList<>();
        String author = UserGlobalService.getCurrentUser().getUsername();
        for (Map.Entry<Integer, HumanDTO> humanDTO : humanDTOs.entrySet()) {
            ResponseEntity<?> resp = checker.validate(humanDTO.getValue());
            if (resp.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().body("Humans: Line " + humanDTO.getKey() + ": " + resp.getBody());
            }
            Human human = new Human();
            human.setAuthor(author);
            HumanDTO.setHuman(human, humanDTO.getValue());
            if (humanDTO.getValue().getCarId() == null){
                human.setCar(carService.getObjWhileAddFunc(
                        new CarDTO(humanDTO.getValue().getCarName(), humanDTO.getValue().isCarIsCool()
                )));
                ret_arr[CounterIndex.CAR.getValue()]++;
            } else {
                Car car = carService.findById(humanDTO.getValue().getCarId());
                if (car == null) {
                    return ResponseEntity.badRequest().body("Humans: Line " + humanDTO.getKey() + ": " + "Error: Car not found");
                }
                human.setCar(car);
            }
            if (humanDTO.getValue().getCoordinatesId() == null){
                human.setCoordinates(coordinatesService.getObjWhileAddFunc(
                        new CoordinatesDTO(humanDTO.getValue().getX(), humanDTO.getValue().getY()
                )));
                ret_arr[CounterIndex.COORDINATES.getValue()]++;
            } else {
                Coordinates coordinates = coordinatesService.findById(humanDTO.getValue().getCoordinatesId());
                if (coordinates == null) {
                    return ResponseEntity.badRequest().body("Humans: Line " + humanDTO.getKey() + ": " + "Error: Coordinates not found");
                }
                human.setCoordinates(coordinates);
            }
            if (!humans.add(human)) {
                return ResponseEntity.badRequest().body("Humans: Line " + humanDTO.getKey() + ": Error: Human with name " + human.getName() + " already exists");
            }
            newHumans.add(human);
        }
        List<Human> savedHumans = repository.saveAll(newHumans);
//        simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedHumans.stream().map(Human::getId).collect(Collectors.toList()), EntityType.HUMAN, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body((String) resp.getBody());
        }
        ret_arr[CounterIndex.HUMAN.getValue()] = savedHumans.size();
        return ResponseEntity.ok(ret_arr);
    }
}
