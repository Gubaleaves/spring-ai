package com.example.openai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "weather")
public record WeatherConfig(String apiKey, String apiUrl) {
}
