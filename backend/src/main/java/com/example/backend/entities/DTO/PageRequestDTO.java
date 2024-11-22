package com.example.backend.entities.DTO;

public class PageRequestDTO {
    private int page;
    private int size;
    private String sortBy;
    private boolean order;

    public PageRequestDTO(int page, int size, String sortBy, boolean order) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}

