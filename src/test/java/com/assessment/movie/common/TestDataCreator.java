package com.assessment.movie.common;

import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.entity.MovieEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataCreator {

    private TestDataCreator(){

    }

    public static MovieEntity dummyMovieEntity(Long id){
        return MovieEntity.builder()
                .id(id)
                .title("The Matrix " + id)
                .category("Novel " + id)
                .starRating(BigDecimal.valueOf(id % 5))
                .createdDate(LocalDateTime.of(2023, 11,28, 1,1,1))
                .updatedDate(LocalDateTime.of(2023, 11,28, 3,3,3))
                .build();
    }

    public static List<MovieEntity> dummyMovieEntities(int size) {
        List<MovieEntity> movies = new ArrayList<>();
        for (long i = 1; i <= size; i++) {
            movies.add(MovieEntity.builder()
                    .id(i)
                    .title("The Matrix " + i)
                    .category("Novel " + i)
                    .starRating(BigDecimal.valueOf(i % 5))
                    .build());
        }
        return movies;
    }

    public static MovieRequest dummyMovieRequest(Long id){
        return MovieRequest.builder()
                .title("The Matrix " + id)
                .category("Novel " + id)
                .starRating(BigDecimal.valueOf(id % 5))
                .build();
    }

    public static MovieResponse dummyMovieResponse(Long id){
        return MovieResponse.builder()
                .id(id)
                .title("The Matrix " + id)
                .category("Novel " + id)
                .starRating(BigDecimal.valueOf(id % 5))
                .createdDate(LocalDateTime.of(2023, 11,28, 1,1,1))
                .updatedDate(LocalDateTime.of(2023, 11,28, 3,3,3))
                .build();
    }


}
