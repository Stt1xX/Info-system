package com.example.shared_module.entities;

import com.example.shared_module.entities.Anntotations.SearchableField;
import com.example.shared_module.entities.Anntotations.SortableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car extends ManagedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq_gen")
    @SequenceGenerator(name = "car_seq_gen", sequenceName = "car_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @SortableField(name = "id")
    @SearchableField(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    @SortableField(name = "name")
    @SearchableField(name = "name")
    private String name;

    @Column(name = "cool")
    private Boolean cool;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isCool() {
        return cool;
    }

    public void setCool(Boolean cool) {
        this.cool = cool;
    }

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