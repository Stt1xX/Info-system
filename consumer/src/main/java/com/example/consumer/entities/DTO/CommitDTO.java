package com.example.consumer.entities.DTO;

import lombok.Data;

@Data
public class CommitDTO {
    private String date;
    private String author;
    private String action;

    public CommitDTO(String date, String author, String action) {
        this.date = date;
        this.author = author;
        this.action = action;
    }
}
