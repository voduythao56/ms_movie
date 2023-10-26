package com.assessment.movie.unit.controller;

import com.assessment.movie.MovieApplication;
import com.assessment.movie.common.TestDataCreator;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.exception.ErrorCode;
import com.assessment.movie.exception.NoMovieFoundException;
import com.assessment.movie.repository.CriteriaNoCountRepository;
import com.assessment.movie.repository.MovieRepository;
import com.assessment.movie.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MovieApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ActiveProfiles("test")
class MovieControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private CriteriaNoCountRepository criteriaNoCountRepository;


    @Test
    void shouldCreateMovieSuccessfully() throws Exception {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.create(movieRequest)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/movies")
                        .content(om.writeValueAsString(movieRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedResponse.getId().intValue())))
                .andExpect(jsonPath("$.title", is(expectedResponse.getTitle())))
                .andExpect(jsonPath("$.category", is(expectedResponse.getCategory())))
                .andExpect(jsonPath("$.starRating", is(expectedResponse.getStarRating().doubleValue())))
                .andExpect(jsonPath("$.createdDate", is(expectedResponse.getCreatedDate().toString())))
                .andExpect(jsonPath("$.updatedDate", is(expectedResponse.getUpdatedDate().toString())));

        verify(movieService, times(1)).create(movieRequest);
    }

    @Test
    void shouldCreateMovieFailWithInvalidTitle() throws Exception {
        /**
         * 1. Invalid title
         */
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setTitle(null);
        assertCreateMovieFailWithInvalidField(movieRequest, "title", "null", "must not be blank");

        String longTitle = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d0000iiiiiooooooo";
        movieRequest.setTitle(longTitle);
        assertCreateMovieFailWithInvalidField(movieRequest, "title", longTitle, "size must be between 1 and 200");

        /**
         * 2. Invalid category
         */
        movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setCategory(null);
        assertCreateMovieFailWithInvalidField(movieRequest, "category", "null", "must not be blank");

        String longCategory = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d0000iiiiiooooooo";
        movieRequest.setCategory(longCategory);
        assertCreateMovieFailWithInvalidField(movieRequest, "category", longCategory, "size must be between 1 and 200");

        /**
         * 3. Invalid starRating
         */
        movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setStarRating(null);
        assertCreateMovieFailWithInvalidField(movieRequest, "starRating", "null", "must not be null");

        movieRequest.setStarRating(BigDecimal.valueOf(9.5));
        assertCreateMovieFailWithInvalidField(movieRequest, "starRating", "9.5", "must be less than or equal to 5.0");

        movieRequest.setStarRating(BigDecimal.valueOf(1.0999));
        assertCreateMovieFailWithInvalidField(movieRequest, "starRating", "1.0999", "numeric value out of bounds (<1 digits>.<1 digits> expected)");
    }

    private void assertCreateMovieFailWithInvalidField(MovieRequest movieRequest, String fieldName, String value, String expectedIssue) throws Exception {
        mockMvc.perform(post("/api/v1/movies")
                        .content(om.writeValueAsString(movieRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details[0].field", is(fieldName)))
                .andExpect(jsonPath("$.details[0].value", is(value)))
                .andExpect(jsonPath("$.details[0].issue", is(expectedIssue)));

        verify(movieService, times(0)).create(movieRequest);
    }

    @Test
    void shouldGetMovieSuccessfully() throws Exception {
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.get(1L)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/movies/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResponse.getId().intValue())))
                .andExpect(jsonPath("$.title", is(expectedResponse.getTitle())))
                .andExpect(jsonPath("$.category", is(expectedResponse.getCategory())))
                .andExpect(jsonPath("$.starRating", is(expectedResponse.getStarRating().doubleValue())))
                .andExpect(jsonPath("$.createdDate", is(expectedResponse.getCreatedDate().toString())))
                .andExpect(jsonPath("$.updatedDate", is(expectedResponse.getUpdatedDate().toString())));

        verify(movieService, times(1)).get(1L);
    }

    @Test
    void shouldGetMovieFailWithInvalidId() throws Exception {
        assertGetMovieFailWithInvalidField("-1", "must be greater than or equal to 1");
        assertGetMovieFailWithInvalidField("-1a", "'id' should be a valid 'Long' and '-1a' isn't");
        assertGetMovieFailWithInvalidField("1212121121212121212121", "'id' should be a valid 'Long' and '1212121121212121212121' isn't");
    }

    private void assertGetMovieFailWithInvalidField(String idValue, String expectedIssue) throws Exception {
        mockMvc.perform(get("/api/v1/movies/" + idValue))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details[0].field", is("id")))
                .andExpect(jsonPath("$.details[0].value", is(idValue)))
                .andExpect(jsonPath("$.details[0].issue", is(expectedIssue)));

        verify(movieService, times(0)).get(any(Long.class));
    }

    @Test
    void shouldGetMovieFailBecauseNoMovieFound() throws Exception {
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.get(1L)).thenThrow(new NoMovieFoundException());

        mockMvc.perform(get("/api/v1/movies/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.NO_MOVIE_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.NO_MOVIE_FOUND.getDescription())));

        verify(movieService, times(1)).get(1L);
    }

    @Test
    void shouldUpdateMovieSuccessfully() throws Exception {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.update(1L, movieRequest)).thenReturn(expectedResponse);

        mockMvc.perform(put("/api/v1/movies/1")
                        .content(om.writeValueAsString(movieRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedResponse.getId().intValue())))
                .andExpect(jsonPath("$.title", is(expectedResponse.getTitle())))
                .andExpect(jsonPath("$.category", is(expectedResponse.getCategory())))
                .andExpect(jsonPath("$.starRating", is(expectedResponse.getStarRating().doubleValue())))
                .andExpect(jsonPath("$.createdDate", is(expectedResponse.getCreatedDate().toString())))
                .andExpect(jsonPath("$.updatedDate", is(expectedResponse.getUpdatedDate().toString())));

        verify(movieService, times(1)).update(1L, movieRequest);
    }


    @Test
    void shouldUpdateMovieFailWithInvalidInput() throws Exception {
        /**
         * 1. Invalid title
         */
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setTitle(null);
        assertUpdateMovieFailWithInvalidField(movieRequest, "title", "null", "must not be blank");

        String longTitle = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d0000iiiiiooooooo";
        movieRequest.setTitle(longTitle);
        assertUpdateMovieFailWithInvalidField(movieRequest, "title", longTitle, "size must be between 1 and 200");

        /**
         * 2. Invalid category
         */
        movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setCategory(null);
        assertUpdateMovieFailWithInvalidField(movieRequest, "category", "null", "must not be blank");

        String longCategory = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d0000iiiiiooooooo";
        movieRequest.setCategory(longCategory);
        assertUpdateMovieFailWithInvalidField(movieRequest, "category", longCategory, "size must be between 1 and 200");

        /**
         * 3. Invalid starRating
         */
        movieRequest = TestDataCreator.dummyMovieRequest(1L);
        movieRequest.setStarRating(null);
        assertUpdateMovieFailWithInvalidField(movieRequest, "starRating", "null", "must not be null");

        movieRequest.setStarRating(BigDecimal.valueOf(9.5));
        assertUpdateMovieFailWithInvalidField(movieRequest, "starRating", "9.5", "must be less than or equal to 5.0");

        movieRequest.setStarRating(BigDecimal.valueOf(1.0999));
        assertUpdateMovieFailWithInvalidField(movieRequest, "starRating", "1.0999", "numeric value out of bounds (<1 digits>.<1 digits> expected)");
    }

    private void assertUpdateMovieFailWithInvalidField(MovieRequest movieRequest, String fieldName, String value, String expectedIssue) throws Exception {
        mockMvc.perform(put("/api/v1/movies/1")
                        .content(om.writeValueAsString(movieRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details[0].field", is(fieldName)))
                .andExpect(jsonPath("$.details[0].value", is(value)))
                .andExpect(jsonPath("$.details[0].issue", is(expectedIssue)));

        verify(movieService, times(0)).update(1L, movieRequest);
    }

    @Test
    void shouldUpdateMovieFailBecauseNoMovieFound() throws Exception {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        when(movieService.update(1L, movieRequest)).thenThrow(new NoMovieFoundException());

        mockMvc.perform(put("/api/v1/movies/1")
                        .content(om.writeValueAsString(movieRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.NO_MOVIE_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.NO_MOVIE_FOUND.getDescription())));

        verify(movieService, times(1)).update(1L, movieRequest);
    }

    @Test
    void shouldDeleteMovieSuccessfully() throws Exception {
        doNothing().when(movieService).delete(1L);

        mockMvc.perform(delete("/api/v1/movies/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).delete(1L);
    }

    @Test
    void shouldDeleteMovieFailBecauseNoMovieFound() throws Exception {
        doThrow(new NoMovieFoundException()).when(movieService).delete(1L);

        mockMvc.perform(delete("/api/v1/movies/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.NO_MOVIE_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.NO_MOVIE_FOUND.getDescription())));

        verify(movieService, times(1)).delete(1L);
    }

    @Test
    void shouldGetListMovieSuccessfully() throws Exception {
        GroupMovieResponse groupMovieResponse = GroupMovieResponse
                .builder()
                .page(5)
                .size(7)
                .movies(Collections.singletonList(TestDataCreator.dummyMovieResponse(36L)))
                .build();
        when(movieService.getList(5, 7)).thenReturn(groupMovieResponse);

        mockMvc.perform(get("/api/v1/movies")
                        .param("page", "5")
                        .param("size", "7"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(groupMovieResponse.getPage())))
                .andExpect(jsonPath("$.size", is(groupMovieResponse.getSize())))
                .andExpect(jsonPath("$.movies[0].id", is(groupMovieResponse.getMovies().get(0).getId().intValue())))
                .andExpect(jsonPath("$.movies[0].title", is(groupMovieResponse.getMovies().get(0).getTitle())))
                .andExpect(jsonPath("$.movies[0].category", is(groupMovieResponse.getMovies().get(0).getCategory())))
                .andExpect(jsonPath("$.movies[0].starRating", is(groupMovieResponse.getMovies().get(0).getStarRating().doubleValue())))
                .andExpect(jsonPath("$.movies[0].createdDate", is(groupMovieResponse.getMovies().get(0).getCreatedDate().toString())))
                .andExpect(jsonPath("$.movies[0].updatedDate", is(groupMovieResponse.getMovies().get(0).getUpdatedDate().toString())));


        verify(movieService, times(1)).getList(5, 7);
    }

    @Test
    void shouldGetListMovieFailWithInvalidData() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .param("page", "-1")
                        .param("size", "7"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details[0].field", is("page")))
                .andExpect(jsonPath("$.details[0].value", is("-1")))
                .andExpect(jsonPath("$.details[0].issue", is("must be greater than or equal to 0")));

        mockMvc.perform(get("/api/v1/movies")
                        .param("page", "-1")
                        .param("size", "0"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "page", "value", "-1", "issue", "must be greater than or equal to 0"),
                        Map.of("field", "size", "value", "0", "issue", "must be greater than or equal to 1")
                )));

        mockMvc.perform(get("/api/v1/movies")
                        .param("page", "2100000000")
                        .param("size", "2110000000"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.INVALID_INPUT.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_INPUT.getDescription())))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "page", "value", "2100000000", "issue", "must be less than or equal to 2000000000"),
                        Map.of("field", "size", "value", "2110000000", "issue", "must be less than or equal to 1000")
                )));


        verify(movieService, times(0)).getList(anyInt(), anyInt());
    }

}
