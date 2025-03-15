package com.example.consumer.repositories;

import com.example.consumer.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer>, JpaSpecificationExecutor<Coordinates> {
}