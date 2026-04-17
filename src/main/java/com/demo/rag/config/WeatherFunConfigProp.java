package com.demo.rag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(value = "weather")
public record WeatherFunConfigProp(String apiKey, String baseUrl) {

}