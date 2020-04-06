package com.learning.frontier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FrontierApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontierApplication.class, args);
	}

}
