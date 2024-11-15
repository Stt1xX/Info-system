package com.example.backend.controllers;

import com.example.backend.entities.User;
import com.example.backend.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/get_user_info")
    public ResponseEntity<?> getUsername() {
        return userService.getUserInfo();
    }

    @PostMapping("/add_new_user")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/delete_user")
    public void deleteUser(@RequestBody String username) {
        userService.deleteUser(username);
    }
}
