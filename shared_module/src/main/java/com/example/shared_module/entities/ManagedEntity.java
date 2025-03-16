package com.example.shared_module.entities;

import com.example.shared_module.entities.Anntotations.SearchableField;
import com.example.shared_module.entities.Anntotations.SortableField;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ManagedEntity {
    @Column(name="author", nullable = false, length = Integer.MAX_VALUE)
    @SortableField(name = "author")
    @SearchableField(name = "author")
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
