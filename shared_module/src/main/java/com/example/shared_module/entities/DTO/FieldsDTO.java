package com.example.shared_module.entities.DTO;

public class FieldsDTO {

    private String frontName;
    private String backName;

    public FieldsDTO(String frontName, String backName) {
        this.frontName = frontName;
        this.backName = backName;
    }

    public String getFrontName() {
        return frontName;
    }

    public void setFrontName(String frontName) {
        this.frontName = frontName;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }
}
