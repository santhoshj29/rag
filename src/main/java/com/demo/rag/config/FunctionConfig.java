package com.demo.rag.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.demo.rag.service.WeatherFunService;

@Configuration
public class FunctionConfig {

    private final WeatherFunConfigProp weatherFunConfigProp;

    @Autowired
    public FunctionConfig(WeatherFunConfigProp weatherFunConfigProp) {
        this.weatherFunConfigProp = weatherFunConfigProp;
    }


    @Bean
    @Description("Get the current weather conditions for the given city")
    public Function<WeatherFunService.Request, WeatherFunService.Response> currentWeatherFunction() {
        return new WeatherFunService(weatherFunConfigProp);
    }
    

}
