package com.example.consumer.entities.DTO;

import com.example.consumer.entities.Car;
import lombok.Data;

@Data
public class CarDTO {
    private String name;
    private boolean cool;

    public CarDTO(Car car) {
        this.cool = car.getCool();
        this.name = car.getName();
    }

    public CarDTO(){

    }

    public CarDTO(String name, boolean cool) {
        this.name = name;
        this.cool = cool;
    }
}
