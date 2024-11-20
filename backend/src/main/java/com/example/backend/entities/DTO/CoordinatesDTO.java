package com.example.backend.entities.DTO;

public class CoordinatesDTO {
    private double x;
    private float y;

    public CoordinatesDTO(double x, float y) {
        this.x = x;
        this.y = y;
    }

    public CoordinatesDTO() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
