package com.example.backend.repositories;


import com.example.backend.entities.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HumanRepository extends JpaRepository<Human, Integer>, JpaSpecificationExecutor<Human> {
    List<Human> findAllByCar_Id(Integer carId);
    List<Human> findAllByCoordinates_Id(Integer coordinatesId);
}