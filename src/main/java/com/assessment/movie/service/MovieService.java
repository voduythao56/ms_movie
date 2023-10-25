package com.assessment.movie.service;

import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;

public interface MovieService {

    MovieResponse create(MovieRequest movieRequest);

    MovieResponse update(Long id, MovieRequest movieRequest);

    MovieResponse get(Long id);

    GroupMovieResponse getList(int page, int size);

    void delete(Long id);
}
