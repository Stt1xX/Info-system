package com.example.shared_module.entities.DTO;

import com.example.shared_module.entities.Car;

public class CarDTO {
    private String name;
    private boolean cool;

    public CarDTO(Car car) {
        this.cool = car.isCool();
        this.name = car.getName();
    }

    public CarDTO(){

    }

    public CarDTO(String name, boolean cool) {
        this.name = name;
        this.cool = cool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCool() {
        return cool;
    }

    public void setCool(boolean cool) {
        this.cool = cool;
    }
}
