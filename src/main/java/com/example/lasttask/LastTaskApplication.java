package com.example.lasttask;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class LastTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastTaskApplication.class, args);
    }

}
