package com.example.backend.entities.DTO;

import org.springframework.hateoas.PagedModel;

import java.util.List;

public class PageResponseDTO<DataClass> {
    private List<DataClass> content;
    private PagedModel.PageMetadata page;

    public PageResponseDTO(List<DataClass> content, PagedModel.PageMetadata page) {
        this.content = content;
        this.page = page;
    }

    public List<DataClass> getContent() {
        return content;
    }

    public void setContent(List<DataClass> content) {
        this.content = content;
    }

    public PagedModel.PageMetadata getPage() {
        return page;
    }

    public void setPage(PagedModel.PageMetadata page) {
        this.page = page;
    }
}
