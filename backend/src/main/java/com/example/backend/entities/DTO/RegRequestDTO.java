package com.example.backend.entities.DTO;

import com.example.backend.entities.Request;
import com.example.backend.utils.Utils;

public class RegRequestDTO {

    private Integer id;
    private String username;
    private String date;

    public RegRequestDTO(Request request) {
        this.id = request.getId();
        this.username = request.getUsername();
        this.date = Utils.prepareDate(request.getDate());
    }

    public static RegRequestDTO convertToDTO(Request request) {
        return new RegRequestDTO(request);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
