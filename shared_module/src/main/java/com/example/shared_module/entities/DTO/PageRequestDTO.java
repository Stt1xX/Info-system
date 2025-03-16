package com.example.shared_module.entities.DTO;

public class PageRequestDTO {
    private int page;
    private int size;
    private String sortBy;
    private boolean order;
    private String template;
    private String varName;

    public PageRequestDTO(int page, int size, String sortBy, boolean order, String template, String varName) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.order = order;
        this.template = template;
        this.varName = varName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
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

