package com.example.shared_module.repositories;

import com.example.shared_module.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer>, JpaSpecificationExecutor<Coordinates> {
}