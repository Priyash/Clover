package com.clover.store.clover.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clover.store.messagequeueservice")
//https://reflectoring.io/spring-boot-kafka/
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
