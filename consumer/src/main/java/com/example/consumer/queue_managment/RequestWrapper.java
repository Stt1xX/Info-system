package com.example.consumer.queue_managment;

import com.example.consumer.entities.User;

import java.io.Serializable;


public record RequestWrapper(User user, byte[] file, String fileName) implements Serializable {

}
