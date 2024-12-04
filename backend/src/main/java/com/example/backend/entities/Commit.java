package com.example.backend.entities;

import com.example.backend.entities.enums.EntityType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EntityType getItemType() {
        return itemType;
    }

    public void setItemType(EntityType itemType) {
        this.itemType = itemType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

}