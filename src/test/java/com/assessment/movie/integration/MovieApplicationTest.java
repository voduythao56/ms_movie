package com.assessment.movie.integration;

import com.assessment.movie.common.TestDataCreator;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.math.RoundingMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("it-test")
public class MovieApplicationTest {

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"));
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.flyway.enabled", () -> "true");

    }

    @BeforeAll
    static void beforeAll() {
        if (!mySQLContainer.isRunning()) {
            mySQLContainer.start();
        }

    }


    @AfterAll
    static void tearDown() {
        if (mySQLContainer.isRunning()) {
            mySQLContainer.stop();
        }
    }

    @Test
    void shouldCreateMovieSuccessfully() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);


        HttpEntity<MovieRequest> request = new HttpEntity<>(movieRequest);
        ResponseEntity<MovieResponse> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ms-movie/api/v1/movies", request, MovieResponse.class);

        MovieResponse response = responseEntity.getBody();
        assert response != null;

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals(movieRequest.getTitle(), response.getTitle());
        Assertions.assertEquals(movieRequest.getCategory(), response.getCategory());
        Assertions.assertEquals(movieRequest.getStarRating().setScale(1, RoundingMode.HALF_UP), response.getStarRating());
        Assertions.assertNotNull(response.getCreatedDate());
        Assertions.assertNotNull(response.getUpdatedDate());
    }


    @Test
    void shouldCreateMovieFailWithInvalidData() throws JsonProcessingException {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setTitle(null);


        HttpEntity<MovieRequest> request = new HttpEntity<>(movieRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ms-movie/api/v1/movies", request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode response = objectMapper.readTree(responseEntity.getBody());

        Assertions.assertEquals(response.path("code").asText(), ErrorCode.INVALID_INPUT.getCode());
        Assertions.assertEquals(response.path("message").asText(), ErrorCode.INVALID_INPUT.getDescription());
        Assertions.assertEquals(response.path("details").get(0).path("field").asText(), "title");
        Assertions.assertEquals(response.path("details").get(0).path("value").asText(), "null");
        Assertions.assertEquals(response.path("details").get(0).path("issue").asText(), "must not be blank");
        Assertions.assertEquals(response.path("details").size(), 1);
    }
}
