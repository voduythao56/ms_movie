package com.assessment.movie.mapper;

import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.entity.MovieEntity;
import lombok.experimental.UtilityClass;

import java.math.RoundingMode;

@UtilityClass
public class MovieMapper {

    public static MovieEntity requestToEntity(MovieRequest movieRequest) {
        if ( movieRequest == null ) {
            return null;
        }

        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(movieRequest.getTitle());
        movieEntity.setCategory(movieRequest.getCategory());
        movieEntity.setStarRating(movieRequest.getStarRating());

        return movieEntity;
    }

    public static void requestToUpdatingEntity(MovieRequest source, MovieEntity destination) {
        if ( source == null || destination == null) {
            return;
        }
        destination.setTitle(source.getTitle());
        destination.setCategory(source.getCategory());
        destination.setStarRating(source.getStarRating());
    }

    public static MovieResponse entityToResponse(MovieEntity movieEntity) {
        if ( movieEntity == null ) {
            return null;
        }

        return MovieResponse
                .builder()
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .category(movieEntity.getCategory())
                .starRating(movieEntity.getStarRating().setScale(1, RoundingMode.HALF_UP))
                .createdDate(movieEntity.getCreatedDate())
                .updatedDate(movieEntity.getUpdatedDate())
                .build();

    }

}
