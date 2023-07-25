package com.app.myproject;

import com.app.myproject.entity.*;
import com.app.myproject.service.UserService;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.Adler32;

@SpringBootTest
@RequiredArgsConstructor
class MyprojectApplicationTests {

	private final UserService service;


	@Test
	void contextLoads() {





	}

	@Test
	void databaseTests() {



	}

}
