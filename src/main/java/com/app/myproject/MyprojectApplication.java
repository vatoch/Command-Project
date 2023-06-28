package com.app.myproject;


import com.app.myproject.config.MyConf;
import com.app.myproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootApplication
@RequiredArgsConstructor
public class MyprojectApplication {

	@Bean
	public WebClient client() {
		return WebClient.builder().build();
	}

	@Bean()
	ObjectMapper mapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext  co = SpringApplication.run(MyprojectApplication.class, args);
		System.out.println(UserService.reverse("123vato4kuba"));



	}

}
