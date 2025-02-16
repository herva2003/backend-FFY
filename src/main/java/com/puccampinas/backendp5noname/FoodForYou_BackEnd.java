package com.puccampinas.backendp5noname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FoodForYou_BackEnd {

	public static void main(String[] args) {
		SpringApplication.run(FoodForYou_BackEnd.class, args);
	}

}
