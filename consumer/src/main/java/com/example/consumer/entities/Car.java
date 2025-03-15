package com.example.consumer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "cars")
public class Car extends ManagedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq_gen")
    @SequenceGenerator(name = "car_seq_gen", sequenceName = "car_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "cool")
    private Boolean cool;

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cool=" + cool +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(name, car.name);
    }



    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}