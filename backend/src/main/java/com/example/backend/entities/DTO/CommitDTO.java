package com.example.backend.entities.DTO;

public class CommitDTO {
    private String date;
    private String author;
    private String action;

    public CommitDTO(String date, String author, String action) {
        this.date = date;
        this.author = author;
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
