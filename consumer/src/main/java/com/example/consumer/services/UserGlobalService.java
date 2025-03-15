package com.example.consumer.services;

import com.example.consumer.entities.User;
import lombok.Getter;

public class UserGlobalService {

    @Getter
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        UserGlobalService.currentUser = currentUser;
    }
}
