package com.rpe.desafio.rpe_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RpeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpeApiApplication.class, args);
	}

}
