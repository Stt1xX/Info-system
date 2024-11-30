package com.example.backend.repositories;

import com.example.backend.entities.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HumanRepository extends JpaRepository<Human, Integer>, JpaSpecificationExecutor<Human> {
}