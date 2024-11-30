package com.example.backend.repositories;

import com.example.backend.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer>, JpaSpecificationExecutor<Coordinates> {
}