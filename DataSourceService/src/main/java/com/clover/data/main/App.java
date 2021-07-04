package com.clover.data.main;

import com.clover.data.utility.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clover.data")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}

