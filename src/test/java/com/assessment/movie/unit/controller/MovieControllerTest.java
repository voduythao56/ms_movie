package com.assessment.movie.unit.controller;

import com.assessment.movie.common.TestDataCreator;
import com.assessment.movie.controller.MovieController;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.service.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;


    @Test
    void shouldCreateMovieSuccessfully() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.create(movieRequest)).thenReturn(expectedResponse);

        ResponseEntity<MovieResponse> responseEntity = movieController.create(movieRequest);

        MovieResponse result = responseEntity.getBody();

        assert result != null;

        assertMovie(expectedResponse, result);
    }

    @Test
    void shouldGetMovieByIdSuccessfully() {
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.get(1L)).thenReturn(expectedResponse);

        ResponseEntity<MovieResponse> responseEntity = movieController.get(1L);

        MovieResponse result = responseEntity.getBody();

        assert result != null;

        assertMovie(expectedResponse, result);
    }

    @Test
    void shouldUpdateMovieSuccessfully() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieResponse expectedResponse = TestDataCreator.dummyMovieResponse(1L);
        when(movieService.update(1L, movieRequest)).thenReturn(expectedResponse);

        ResponseEntity<MovieResponse> responseEntity = movieController.update(1L, movieRequest);

        MovieResponse result = responseEntity.getBody();

        assert result != null;

        assertMovie(expectedResponse, result);
    }

    @Test
    void shouldDeleteMovieSuccessfully() {
        doNothing().when(movieService).delete(1L);

        movieController.delete(1L);

        verify(movieService).delete(1L);
    }

    @Test
    void shouldGetListMovieSuccessfully() {
        GroupMovieResponse groupMovieResponse = GroupMovieResponse
                .builder()
                .page(5)
                .size(7)
                .movies(Collections.singletonList(TestDataCreator.dummyMovieResponse(36L)))
                .build();
        when(movieService.getList(5, 7)).thenReturn(groupMovieResponse);

        ResponseEntity<GroupMovieResponse> responseEntity = movieController.getList(5, 7);

        GroupMovieResponse result = responseEntity.getBody();

        assert result != null;
        assert result.getMovies() != null;

        Assertions.assertEquals(groupMovieResponse.getMovies().size(), result.getMovies().size());

        for (int i = 0; i < groupMovieResponse.getMovies().size(); i++) {
            assertMovie(groupMovieResponse.getMovies().get(i), result.getMovies().get(i));
        }
        Assertions.assertEquals(groupMovieResponse.getPage(), result.getPage());
        Assertions.assertEquals(groupMovieResponse.getSize(), result.getSize());

    }

    private void assertMovie(MovieResponse expectedResponse, MovieResponse result) {
        Assertions.assertEquals(expectedResponse.getId(), result.getId());
        Assertions.assertEquals(expectedResponse.getTitle(), result.getTitle());
        Assertions.assertEquals(expectedResponse.getCategory(), result.getCategory());
        Assertions.assertEquals(expectedResponse.getStarRating(), result.getStarRating());
        Assertions.assertEquals(expectedResponse.getCreatedDate(), result.getCreatedDate());
        Assertions.assertEquals(expectedResponse.getUpdatedDate(), result.getUpdatedDate());
    }

}
