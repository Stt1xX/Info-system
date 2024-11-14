package com.example.backend.controllers;

import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }
}
