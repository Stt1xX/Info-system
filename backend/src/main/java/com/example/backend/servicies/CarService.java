package com.example.backend.servicies;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.repositories.CarRepository;
import com.example.backend.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final UserService userService;
        private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public CarService(CarRepository carRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    public ResponseEntity<?> addCar(CarDTO carDTO) {
        ResponseEntity<?> resp = Validator.validate(carDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Car car = new Car();
        car.setAuthor(userService.getCurrentUsername());
        car.setCool(carDTO.isCool());
        car.setName(carDTO.getName());
        try{
            carRepository.save(car);
            simpMessagingTemplate.convertAndSend("/topic/cars", getAllCars());
            return new ResponseEntity<>(String.format("Car %s successfully added!", car.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect car's input data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> updateCar(Integer id, CarDTO carDTO) {
        ResponseEntity<?> resp = Validator.validate(carDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        car.setAuthor(userService.getCurrentUsername());
        car.setCool(carDTO.isCool());
        car.setName(carDTO.getName());
        try{
            carRepository.save(car);
            simpMessagingTemplate.convertAndSend("/topic/cars", getAllCars());
            return new ResponseEntity<>(String.format("Car %s successfully updated!", car.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect car's input data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> deleteCar(Integer id) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        carRepository.delete(car);
        simpMessagingTemplate.convertAndSend("/topic/cars", getAllCars());
        return new ResponseEntity<>(String.format("Car %s successfully deleted!", car.getName()), HttpStatus.OK);
    }


}
