package com.example.consumer.entities;

import com.example.consumer.entities.enums.EntityType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "commits")
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('commits_commit_id_seq')")
    @Column(name = "commit_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private EntityType itemType;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name="author", nullable = false, length = Integer.MAX_VALUE)
    private String author;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "action", nullable = false)
    private String action;

}