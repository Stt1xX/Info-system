package com.example.backend.servicies;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarService extends ItemService<CarDTO, Car> {

    private final HumanService humanService;

    @Autowired
    public CarService(CarRepository carRepository, UserService userService, AuditService auditService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, HumanService humanService) {
        super(carRepository, userService, auditService, simpMessagingTemplate, checker, Car.class);
        this.humanService = humanService;
    }

    @Override
    public ResponseEntity<?> add(CarDTO carDTO) {
        ResponseEntity<?> resp = checker.validate(carDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Car car = new Car();
        car.setAuthor(userService.getCurrentUser().getUsername());
        car.setCool(carDTO.isCool());
        car.setName(carDTO.getName());
        try{
            Car savedCar = repository.save(car);
            simpMessagingTemplate.convertAndSend("/topic/cars", getSocketMessage());
            resp = auditService.doCommit(savedCar.getId(), EntityType.CAR, "Create");
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
            return new ResponseEntity<>(String.format("Car %s successfully added!", car.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect car's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, CarDTO carDTO) {
        ResponseEntity<?> resp = checker.validate(carDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Car car = repository.findById(id).orElse(null);
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        resp = checker.check_rights(car);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        car.setCool(carDTO.isCool());
        car.setName(carDTO.getName());
        try{
            List<Human> depends = getDepends(id);
            Car updatedCar = repository.save(car);
            simpMessagingTemplate.convertAndSend("/topic/cars", getSocketMessage());
            if (!depends.isEmpty())
                simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
            resp = auditService.doCommit(updatedCar.getId(), EntityType.CAR, "Update");
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
            return new ResponseEntity<>(String.format("Car %s successfully updated!", car.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect car's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Car car = repository.findById(id).orElse(null);
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<?> resp = checker.check_rights(car);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        List<Human> depends = getDepends(id);
        repository.delete(car);
        if (!depends.isEmpty()){
            for (Human human : depends) {
                auditService.deleteCommits(human.getId(), EntityType.HUMAN);
            }
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        }
        auditService.deleteCommits(id, EntityType.CAR);
        simpMessagingTemplate.convertAndSend("/topic/cars", getSocketMessage());
        return new ResponseEntity<>(String.format("Car %s successfully deleted!", car.getName()), HttpStatus.OK);
    }

    public List<Human> getDepends(Integer id) {
        return humanService.findByCarId(id);
    }

    public Car findByName(String name) {
        return ((CarRepository) (repository)).findCarByName(name);
    }

    public Car save(Car car) {
        return repository.save(car);
    }
}
