package com.assessment.movie.unit.service;

import com.assessment.movie.common.TestDataCreator;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.entity.MovieEntity;
import com.assessment.movie.exception.ErrorCode;
import com.assessment.movie.exception.NoMovieFoundException;
import com.assessment.movie.repository.CriteriaNoCountRepository;
import com.assessment.movie.repository.MovieRepository;
import com.assessment.movie.service.MovieServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CriteriaNoCountRepository criteriaNoCountRepository;

    @Test
    void shouldCreateMovieSuccessfully() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieEntity movieEntity = TestDataCreator.dummyMovieEntity(1L);
        when(movieRepository.save(any(MovieEntity.class))).thenReturn(movieEntity);

        MovieResponse result = movieService.create(movieRequest);

        assertMovie(movieEntity, result);
    }

    @Test
    void shouldUpdateMovieSuccessfully() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        MovieEntity movieEntity = TestDataCreator.dummyMovieEntity(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(mock(MovieEntity.class)));
        when(movieRepository.save(any(MovieEntity.class))).thenReturn(movieEntity);

        MovieResponse result = movieService.update(1L, movieRequest);

        assertMovie(movieEntity, result);
    }

    @Test
    void shouldUpdateMovieFailBecauseNoMovieFound() {
        MovieRequest movieRequest = TestDataCreator.dummyMovieRequest(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        NoMovieFoundException exception = Assertions.assertThrows(NoMovieFoundException.class, () -> movieService.update(1L, movieRequest));

        Assertions.assertEquals(ErrorCode.NO_MOVIE_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldGetMovieSuccessfully() {
        MovieEntity movieEntity = TestDataCreator.dummyMovieEntity(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movieEntity));

        MovieResponse result = movieService.get(1L);

        assertMovie(movieEntity, result);
    }

    @Test
    void shouldGetMovieFailBecauseNoMovieFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        NoMovieFoundException exception = Assertions.assertThrows(NoMovieFoundException.class, () -> movieService.get(1L));

        Assertions.assertEquals(ErrorCode.NO_MOVIE_FOUND, exception.getErrorCode());
    }


    @Test
    void shouldDeleteMovieSuccessfully() {
        MovieEntity movieEntity = TestDataCreator.dummyMovieEntity(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movieEntity));

        movieService.delete(1L);

        verify(movieRepository).delete(movieEntity);
    }

    @Test
    void shouldDeleteMovieFailBecauseNoMovieFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        NoMovieFoundException exception = Assertions.assertThrows(NoMovieFoundException.class, () -> movieService.delete(1L));

        Assertions.assertEquals(ErrorCode.NO_MOVIE_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldGetListMovieSuccessfully() {
        int page = 4;
        int size = 12;
        List<MovieEntity> movieEntities = TestDataCreator.dummyMovieEntities(size);
        Page<MovieEntity> pageResult = mock(Page.class);
        when(pageResult.get()).thenReturn(movieEntities.stream());
        when(criteriaNoCountRepository.findAll(any(Pageable.class), eq(MovieEntity.class))).thenReturn(pageResult);

        GroupMovieResponse result = movieService.getList(page, size);

        assert result != null;
        assert result.getMovies() != null;

        Assertions.assertEquals(movieEntities.size(), result.getMovies().size());

        for (int i = 0; i < movieEntities.size(); i++) {
            assertMovie(movieEntities.get(i), result.getMovies().get(i));
        }


    }

    private void assertMovie(MovieEntity expectedResult, MovieResponse actualResult) {
        Assertions.assertEquals(expectedResult.getId(), actualResult.getId());
        Assertions.assertEquals(expectedResult.getTitle(), actualResult.getTitle());
        Assertions.assertEquals(expectedResult.getCategory(), actualResult.getCategory());
        Assertions.assertEquals(expectedResult.getStarRating(), actualResult.getStarRating());
        Assertions.assertEquals(expectedResult.getCreatedDate(), actualResult.getCreatedDate());
        Assertions.assertEquals(expectedResult.getUpdatedDate(), actualResult.getUpdatedDate());
    }

}
