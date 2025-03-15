package com.example.consumer.services;

import com.example.consumer.entities.Car;
import com.example.consumer.entities.DTO.CarDTO;
import com.example.consumer.entities.enums.EntityType;
import com.example.consumer.repositories.CarRepository;
import com.example.consumer.services.enums.CounterIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarService extends ItemService<CarDTO, Car> {

    @Autowired
    public CarService(CarRepository carRepository, AuditService auditService, Checker checker) {
        super(carRepository, auditService, checker, Car.class);
    }

    @Override
    public ResponseEntity<?> add(CarDTO carDTO) {
        ResponseEntity<?> resp = checker.validate(carDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
        getAll().forEach(car -> {
            if (car.getName().equals(carDTO.getName())) {
                throw new DataIntegrityViolationException("Error: Car with name " + carDTO.getName() + " already exists");
            }
        });
        Car car = new Car();
        car.setAuthor(UserGlobalService.getCurrentUser().getUsername());
        car.setCool(carDTO.isCool());
        car.setName(carDTO.getName());
        Car savedCar = repository.save(car);
//        simpMessagingTemplate.convertAndSend("/topic/cars", getSocketMessage());
        resp = auditService.doCommit(savedCar.getId(), EntityType.CAR, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
        return new ResponseEntity<>(List.of(String.format("Car %s successfully added!", car.getName()), savedCar), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addAll(Map<Integer, CarDTO> carDTOs) {
        Set<Car> cars = new HashSet<>(getAll());
        List<Car> newCars = new ArrayList<>();
        String author = UserGlobalService.getCurrentUser().getUsername();
        for (Map.Entry<Integer, CarDTO> carDTO : carDTOs.entrySet()) {
            ResponseEntity<?> resp = checker.validate(carDTO.getValue());
            if (resp.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().body("Cars: Line " + carDTO.getKey() + ": " + resp.getBody());
            }
            Car car = new Car();
            car.setAuthor(author);
            car.setCool(carDTO.getValue().isCool());
            car.setName(carDTO.getValue().getName());
            if (!cars.add(car)) {
                return ResponseEntity.badRequest().body("Cars: Line " + carDTO.getKey() + ": Error: Car with name " + car.getName() + " already exists");
            }
            newCars.add(car);
        }
        List<Car> savedCar = repository.saveAll(newCars);
//        simpMessagingTemplate.convertAndSend("/topic/cars", getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedCar.stream().map(Car::getId).collect(Collectors.toList()), EntityType.CAR, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        int[] ret_arr = new int[]{0, 0, 0};
        ret_arr[CounterIndex.CAR.getValue()] = savedCar.size();
        return ResponseEntity.ok(ret_arr);
    }
}
