package com.clover.store.DiscoveryServer.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = "com.clover.store.DiscoveryServer")
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
