package com.example.admin_web_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AdminWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminWebAppApplication.class, args);
    }
}
