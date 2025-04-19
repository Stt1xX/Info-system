package com.example.shared_module.queue_managment;

import com.example.shared_module.entities.User;

public record RequestWrapper(User user, byte[] file, String fileName) {
}