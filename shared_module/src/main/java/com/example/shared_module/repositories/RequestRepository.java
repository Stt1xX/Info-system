package com.example.shared_module.repositories;

import com.example.shared_module.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findByUsername(String username);
}
