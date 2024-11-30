package com.example.backend.entities;

import com.example.backend.entities.Anntotations.SearchableField;
import com.example.backend.entities.Anntotations.SortableField;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "coordinates")
public class Coordinates extends ManagedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('coordinates_id_seq')")
    @Column(name = "id", nullable = false)
    @SortableField(name = "id")
    @SearchableField(name = "id")
    private Integer id;

    @Column(name = "x")
    @SortableField(name = "coordinate X")
    private Double x;

    @Column(name = "y", nullable = false)
    @SortableField(name = "coordinate Y")
    private Float y;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

}