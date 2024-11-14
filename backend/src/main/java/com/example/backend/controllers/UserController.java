package com.example.backend.controllers;

import com.example.backend.entities.User;
import com.example.backend.exceptions.SameUserExistsException;
import com.example.backend.servicies.UserService;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get_username")
    public String getUsername() {
        return userService.getCurrentUsername();
    }

    @PostMapping("/add_new_user")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }
}
