package com.app.myproject;


import com.app.myproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}



	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("gasd@gmail.com");
		mailSender.setPassword("qwdzxvrg");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}





	public static void main(String[] args) throws InterruptedException {

		ApplicationContext  co = SpringApplication.run(MyprojectApplication.class, args);




	}

}
