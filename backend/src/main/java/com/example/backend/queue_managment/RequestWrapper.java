package com.example.backend.queue_managment;

import com.example.backend.entities.User;

public record RequestWrapper(User user, byte[] file, String fileName) {
}