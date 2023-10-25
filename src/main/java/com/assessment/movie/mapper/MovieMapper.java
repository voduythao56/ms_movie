package com.assessment.movie.mapper;

import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieEntity requestToEntity(MovieRequest movieRequest);

    void requestToUpdatingEntity(MovieRequest movieRequest, @MappingTarget MovieEntity movieEntity);

    MovieResponse entityToResponse(MovieEntity movieEntity);

}
