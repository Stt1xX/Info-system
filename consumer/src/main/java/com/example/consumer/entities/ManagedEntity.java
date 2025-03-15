package com.example.consumer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class ManagedEntity {
    @Column(name="author", nullable = false, length = Integer.MAX_VALUE)
    private String author;
}
