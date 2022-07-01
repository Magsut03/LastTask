package com.example.lasttask;

import com.example.lasttask.config.OpenApiProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition
@EnableConfigurationProperties({
        OpenApiProperties.class
})
public class LastTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastTaskApplication.class, args);
    }

}
