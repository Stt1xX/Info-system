package com.example.consumer.entities.DTO;

import lombok.Data;

@Data
public class CoordinatesDTO {
    private double x;
    private float y;

    public CoordinatesDTO(double x, float y) {
        this.x = x;
        this.y = y;
    }

    public CoordinatesDTO() {
    }
}
