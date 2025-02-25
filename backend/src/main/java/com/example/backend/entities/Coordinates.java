package com.example.backend.entities;

import com.example.backend.entities.Anntotations.SearchableField;
import com.example.backend.entities.Anntotations.SortableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "coordinates")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coordinates extends ManagedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coords_seq_gen")
    @SequenceGenerator(name = "coords_seq_gen", sequenceName = "coords_id_seq", allocationSize = 1)
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}