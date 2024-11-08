package com.example.openai;

import com.example.openai.config.WeatherConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(WeatherConfig.class)
@SpringBootApplication
public class OpenAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenAIApplication.class, args);
    }
}
