package com.example.web_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookstore")
public record ApplicationProperties(String apiGatewayUrl) {}
