package com.github.fabriciolfj.apiecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApiEcommerceApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}

}
