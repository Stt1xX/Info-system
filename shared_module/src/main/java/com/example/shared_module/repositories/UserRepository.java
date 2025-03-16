package com.example.shared_module.repositories;

import com.example.shared_module.entities.User;
import com.example.shared_module.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    void deleteByUsername(String username);
    List<User> findByRole(Role role);
}