package com.example.shared_module.servicies;

import com.example.shared_module.entities.Car;
import com.example.shared_module.entities.Coordinates;
import com.example.shared_module.entities.DTO.CarDTO;
import com.example.shared_module.entities.DTO.CoordinatesDTO;
import com.example.shared_module.entities.DTO.HumanDTO;
import com.example.shared_module.entities.Human;
import com.example.shared_module.entities.User;
import com.example.shared_module.entities.enums.EntityType;
import com.example.shared_module.repositories.HumanRepository;
import com.example.shared_module.servicies.enums.CounterIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HumanService extends ItemService<HumanDTO, Human> {

    private final CarService carService;
    private final CoordinatesService coordinatesService;

    @Autowired
    public HumanService(HumanRepository humanRepository, UserService userService, AuditService auditService,
                        SimpMessagingTemplate simpMessagingTemplate, Checker checker,
                        CarService carService, CoordinatesService coordinatesService) {
        super(humanRepository, userService, auditService, simpMessagingTemplate, checker, Human.class);
        this.carService = carService;
        this.coordinatesService = coordinatesService;
    }

    @Override
    public ResponseEntity<?> add(HumanDTO humanDTO, boolean shouldTrigger, User user) {
        ResponseEntity<?> resp = checker.validate(humanDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
        getAll().forEach(human -> {
            if (human.getName().equals(humanDTO.getName())) {
                throw new DataIntegrityViolationException("Error: Human with name " + humanDTO.getName() + " already exists");
            }
        });
        Human human = new Human();
        HumanDTO.setHuman(human, humanDTO);
        Car car = carService.findById(humanDTO.getCarId());
        Coordinates coordinates = coordinatesService.findById(humanDTO.getCoordinatesId());
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        human.setCar(car);
        human.setCoordinates(coordinates);
        human.setAuthor(user.getUsername());
        Human updatedHuman = repository.save(human);
        if (shouldTrigger){
            simpMessagingTemplate.convertAndSend("/topic/humans", userService.getSocketMessage());
        }
        resp = auditService.doCommit(updatedHuman.getId(), EntityType.HUMAN, "Create", shouldTrigger, user);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        return new ResponseEntity<>(List.of(String.format("Human %s successfully added!", human.getName()), updatedHuman), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addAll(Map<Integer, HumanDTO> humanDTOs, User user) {
        int[] ret_arr = new int[]{0, 0, 0};
        Set<Human> humans = new HashSet<>(getAll());
        List<Human> newHumans = new ArrayList<>();
        String author = user.getUsername();
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
                        new CarDTO(humanDTO.getValue().getCarName(), humanDTO.getValue().getCarIsCool()
                ), user));
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
                ), user));
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
        simpMessagingTemplate.convertAndSend("/topic/humans", userService.getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedHumans.stream().map(Human::getId).collect(Collectors.toList()), EntityType.HUMAN, "Create", user);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body((String) resp.getBody());
        }
        ret_arr[CounterIndex.HUMAN.getValue()] = savedHumans.size();
        return ResponseEntity.ok(ret_arr);
    }

    @Override
    public ResponseEntity<?> update(Integer id, HumanDTO humanDTO) {
        ResponseEntity<?> resp = checker.validate(humanDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Human human = repository.findById(id).orElse(null);
        if (human == null) {
            return new ResponseEntity<>("Error: Human not found", HttpStatus.NOT_FOUND);
        }
        resp = checker.check_rights(human);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }

        getAll().stream().filter(_human -> !Objects.equals(_human.getName(), human.getName())).forEach(_human -> {
            if (_human.getName().equals(humanDTO.getName())) {
                throw new DataIntegrityViolationException("Error: Human with name " + humanDTO.getName() + " already exists");
            }
        });
        try {
            HumanDTO.setHuman(human, humanDTO);
            Car car = carService.findById(humanDTO.getCarId());
            Coordinates coordinates = coordinatesService.findById(humanDTO.getCoordinatesId());
            if (car == null) {
                return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
            }
            if (coordinates == null) {
                return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
            }
            human.setCar(car);
            human.setCoordinates(coordinates);
            Human updatedHuman = repository.save(human);
            simpMessagingTemplate.convertAndSend("/topic/humans", userService.getSocketMessage());
            resp = auditService.doCommit(updatedHuman.getId(), EntityType.HUMAN, "Update", true, userService.getCurrentUser());
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
            return new ResponseEntity<>(String.format("Human %s successfully updated!", human.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect human's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Human human = repository.findById(id).orElse(null);
        if (human == null) {
            return new ResponseEntity<>("Error: Human not found", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<?> resp = checker.check_rights(human);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        repository.delete(human);
        auditService.deleteCommits(id, EntityType.HUMAN);
        simpMessagingTemplate.convertAndSend("/topic/humans", userService.getSocketMessage());
        return new ResponseEntity<>(String.format("Human %s successfully deleted!", human.getName()), HttpStatus.OK);
    }

    public void save(Human human) {
        repository.save(human);
    }

    public List<Human> findAllByImpactSpeed(Integer impactSpeed) {
        return ((HumanRepository) (this.repository)).findAllByImpactSpeed(impactSpeed);
    }
}
