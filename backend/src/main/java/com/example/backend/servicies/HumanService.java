package com.example.backend.servicies;

import com.example.backend.entities.Car;
import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.Human;
import com.example.backend.repositories.CarRepository;
import com.example.backend.repositories.CoordinatesRepository;
import com.example.backend.repositories.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HumanService extends ItemService<HumanDTO, Human> {

    private final CarRepository carRepository;
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public HumanService(HumanRepository humanRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, CarRepository carRepository, CoordinatesRepository coordinatesRepository) {
        super(humanRepository, userService, simpMessagingTemplate, checker);
        this.carRepository = carRepository;
        this.coordinatesRepository = coordinatesRepository;
    }

    @Override
    public ResponseEntity<?> add(HumanDTO humanDTO) {
        ResponseEntity<?> resp = checker.validate(humanDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        try{
            Human human = humanDTO.convertToHuman();
            Optional<Car> carOptional = carRepository.findById(humanDTO.getCarId());
            Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(humanDTO.getCoordinatesId());
            if (carOptional.isEmpty()) {
                return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
            }
            if (coordinatesOptional.isEmpty()) {
                return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
            }
            human.setCar(carOptional.get());
            human.setCoordinates(coordinatesOptional.get());
            human.setAuthor(userService.getCurrentUser().getUsername());
            repository.save(human);
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
            return new ResponseEntity<>(String.format("Human %s successfully added!", human.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error: Incorrect human's input data", HttpStatus.CONFLICT);
        }
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
        try {
            HumanDTO.setHuman(human, humanDTO);
            Optional<Car> carOptional = carRepository.findById(humanDTO.getCarId());
            Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(humanDTO.getCoordinatesId());
            if (carOptional.isEmpty()) {
                return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
            }
            if (coordinatesOptional.isEmpty()) {
                return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
            }
            human.setCar(carOptional.get());
            human.setCoordinates(coordinatesOptional.get());
            repository.save(human);
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
            return new ResponseEntity<>(String.format("Human %s successfully updated!", human.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
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
        simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        return new ResponseEntity<>(String.format("Human %s successfully deleted!", human.getName()), HttpStatus.OK);
    }
}
