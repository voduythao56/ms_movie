package com.assessment.movie.service;

import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.entity.MovieEntity;
import com.assessment.movie.exception.NoMovieFoundException;
import com.assessment.movie.mapper.MovieMapper;
import com.assessment.movie.repository.CriteriaNoCountRepository;
import com.assessment.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CriteriaNoCountRepository criteriaNoCountRepository;

    @Override
    @Transactional
    public MovieResponse create(MovieRequest movieRequest) {
        MovieEntity movieEntity = MovieMapper.requestToEntity(movieRequest);
        movieEntity = movieRepository.save(movieEntity);
        return MovieMapper.entityToResponse(movieEntity);
    }

    @Override
    @Transactional
    public MovieResponse update(Long id, MovieRequest movieRequest) {
        MovieEntity movieEntity = getMovieEntity(id);

        MovieMapper.requestToUpdatingEntity(movieRequest, movieEntity);

        movieEntity = movieRepository.save(movieEntity);
        return MovieMapper.entityToResponse(movieEntity);
    }

    @Override
    public MovieResponse get(Long id) {
        MovieEntity movieEntity = getMovieEntity(id);
        return MovieMapper.entityToResponse(movieEntity);
    }

    @Override
    public GroupMovieResponse getList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<MovieEntity> result = criteriaNoCountRepository.findAll(pageable, MovieEntity.class);

        return GroupMovieResponse.builder()
                .movies(result.get().map(MovieMapper::entityToResponse).collect(Collectors.toList()))
                .page(page)
                .size(size)
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MovieEntity movieEntity = getMovieEntity(id);
        movieRepository.delete(movieEntity);
    }

    private MovieEntity getMovieEntity(Long id) {
        Optional<MovieEntity> optMovieEntity = movieRepository.findById(id);
        if (optMovieEntity.isEmpty()) {
            throw new NoMovieFoundException();
        }
        return optMovieEntity.get();
    }
}
