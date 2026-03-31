package com.demo.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class RagApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagApplication.class, args);
	}

}
