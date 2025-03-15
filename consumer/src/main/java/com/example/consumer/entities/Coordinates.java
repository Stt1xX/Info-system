package com.example.consumer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "coordinates")
public class Coordinates extends ManagedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coords_seq_gen")
    @SequenceGenerator(name = "coords_seq_gen", sequenceName = "coords_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "x")
    private Double x;

    @Column(name = "y", nullable = false)
    private Float y;

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