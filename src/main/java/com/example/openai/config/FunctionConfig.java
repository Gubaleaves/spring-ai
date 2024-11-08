package com.example.openai.config;

import com.example.openai.invoker.WeatherInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {

    private final WeatherConfig weatherConfig;

    public FunctionConfig(WeatherConfig weatherConfig) {
        this.weatherConfig = weatherConfig;
    }

    @Bean
    @Description("Get the current weather in location")
    public Function<WeatherInvoker.Request, WeatherInvoker.Response> currentWeather() {
        return new WeatherInvoker(weatherConfig);
    }
}
