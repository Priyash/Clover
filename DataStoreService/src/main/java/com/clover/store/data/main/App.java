package com.clover.store.data.main;

import com.clover.store.data.utility.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.clover.store.datastoreservice")
public class App {
	static {
		try {
			Class.forName(Constants.JSON_ADAPTER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}

