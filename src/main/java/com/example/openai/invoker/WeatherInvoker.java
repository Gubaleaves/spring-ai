package com.example.openai.invoker;

import com.example.openai.config.WeatherConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@Component
public class WeatherInvoker implements Function<WeatherInvoker.Request, WeatherInvoker.Response> {

    private static final Logger log = LoggerFactory.getLogger(WeatherInvoker.class);

    public final RestClient restClient;

    private final WeatherConfig weatherConfig;

    public WeatherInvoker(WeatherConfig weatherConfig) {
        this.weatherConfig = weatherConfig;
        this.restClient = RestClient.create(weatherConfig.apiUrl());
    }

    public record Request(String location) {}

    public record Response(Location location, Current current) {}

    public record Location(String name, String region, String country, Long lat, Long lon) {}

    public record Current(String temp_f, Condition condition, String width_mph, String humidity) {}

    public record Condition(String text) {}

    public Response apply(Request request) {  // 调用第三方接口
        log.info("Using Weather API");
        return restClient
                .get()
                .uri("/current.json?key={key}&q={q}", weatherConfig.apiKey(), request.location)
                .retrieve()
                .body(Response.class);
    }
}
