package com.assessment.movie.controller;

import com.assessment.movie.common.Constants;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api")
@Validated
public class MovieController {

    @Autowired
    private MovieService movieService;


    @RequestMapping(value = "/v1/movies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> create(@RequestBody @Valid MovieRequest movieRequest) {
        MovieResponse movieResponse = movieService.create(movieRequest);
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> update(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id, @RequestBody @Valid MovieRequest movieRequest) {
        MovieResponse movieResponse = movieService.update(id, movieRequest);
        return new ResponseEntity<>(movieResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieResponse> get(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id) {
        MovieResponse movieResponse = movieService.get(id);
        return new ResponseEntity<>(movieResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id) {
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v1/movies")
    public ResponseEntity<GroupMovieResponse> getList(@RequestParam(value = "page", defaultValue = "0", required = false) @Min(0) @Max(Constants.MAX_PAGE_NO) int page,
                                                  @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE_AS_STR, required = false) @Min(1) @Max(Constants.MAX_PAGE_SIZE) int size) {
        GroupMovieResponse groupMovieResponse = movieService.getList(page, size);
        return new ResponseEntity<>(groupMovieResponse, HttpStatus.OK);
    }

}
