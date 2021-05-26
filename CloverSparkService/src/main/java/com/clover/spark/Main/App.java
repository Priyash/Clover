package com.clover.spark.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clover.store.spark.CloverSparkService")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
