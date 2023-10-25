package com.assessment.movie.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
class MovieControllerTest {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
		registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
		registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
		registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
		registry.add("spring.flyway.enabled", () -> "true");
	}

	@Autowired
	private RestTemplate restTemplate;




}
