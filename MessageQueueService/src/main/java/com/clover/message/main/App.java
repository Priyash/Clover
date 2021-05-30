package com.clover.message.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clover.message")
//https://reflectoring.io/spring-boot-kafka/
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
