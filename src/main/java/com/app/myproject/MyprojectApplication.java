package com.app.myproject;


import com.app.myproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;


@SpringBootApplication
@RequiredArgsConstructor
public class MyprojectApplication {

	@Bean
	public WebClient client() {
		return WebClient.builder().build();
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}



	public static void main(String[] args) throws InterruptedException {

		ApplicationContext  co = SpringApplication.run(MyprojectApplication.class, args);




	}

}
