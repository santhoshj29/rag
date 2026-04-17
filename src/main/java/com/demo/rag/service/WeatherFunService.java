package com.demo.rag.service;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.client.RestClient;

import com.demo.rag.config.WeatherFunConfigProp;


public class WeatherFunService implements Function<WeatherFunService.Request, WeatherFunService.Response>{

    private static Logger logger = LoggerFactory.getLogger(WeatherFunService.class);

    private final WeatherFunConfigProp weatherFunConfigProp;
    private final RestClient restClient;

    @Autowired
    public WeatherFunService(WeatherFunConfigProp weatherFunConfigProp) {
        this.weatherFunConfigProp = weatherFunConfigProp;
        this.restClient = RestClient.create(weatherFunConfigProp.baseUrl());
    }


    @Override
    public Response apply(Request request){
        logger.info("Weather request: {}", request);
        Response response = restClient.get().uri("/current.json?key={key}&q={q}", weatherFunConfigProp.apiKey(), request.city()).retrieve().body(Response.class);
        logger.info("Weather API response: {}", response);
        return response;
    }

    public record Request(String city){}
    public record Response(Location location, Current current){}
    public record Location(String name, String region, String country, Double lat, Double lon){}
    public record Current(Double temp_f, Condition condition, Double wind_mph, Double humidity){}
    public record Condition (String text){}


    
}
