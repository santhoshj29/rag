package com.demo.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.shell.command.annotation.CommandScan;

import com.demo.rag.config.WeatherFunConfigProp;

@SpringBootApplication
@CommandScan
@EnableConfigurationProperties(WeatherFunConfigProp.class)
public class RagApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagApplication.class, args);
	}

}
