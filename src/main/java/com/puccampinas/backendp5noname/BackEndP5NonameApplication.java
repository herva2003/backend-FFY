package com.puccampinas.backendp5noname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BackEndP5NonameApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndP5NonameApplication.class, args);
	}

}


