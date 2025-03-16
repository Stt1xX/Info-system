package com.example.backend.controllers;



import com.example.shared_module.entities.Car;
import com.example.shared_module.entities.DTO.CarDTO;
import com.example.shared_module.servicies.CarService;
import com.example.shared_module.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class CarController extends ItemController<CarDTO, Car> {

    @Autowired
    public CarController(CarService service, UserService userService) {
        super(service, Car.class, userService);
    }

    @GetMapping("/get_depends/{id}")
    public Integer getDepends(@PathVariable Integer id) {
        return ((CarService) (itemService)).getDepends(id).size();
    }
}