package com.project.FixMyStreet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FixMyStreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(FixMyStreetApplication.class, args);
	}

}
