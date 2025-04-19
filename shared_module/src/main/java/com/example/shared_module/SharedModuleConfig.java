package com.example.shared_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
@EnableJpaRepositories(basePackages = "com.example.shared_module.repositories")
@EntityScan(basePackages = "com.example.shared_module.entities")
@ComponentScan(basePackages = "com.example.shared_module")
public class SharedModuleConfig {
}