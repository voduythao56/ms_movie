package com.assessment.movie.controller;

import com.assessment.movie.common.Constants;
import com.assessment.movie.dto.request.MovieRequest;
import com.assessment.movie.dto.response.GroupMovieResponse;
import com.assessment.movie.dto.response.MovieResponse;
import com.assessment.movie.service.MovieService;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class MovieController {

    @Autowired
    private MovieService movieService;


    @RequestMapping(value = "/v1/movies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> create(@RequestBody @Valid MovieRequest movieRequest) {
        log.info("Start to create a movie with title={}", movieRequest.getTitle());

        MovieResponse movieResponse = movieService.create(movieRequest);

        log.info("Complete to create a movie with title={}", movieRequest.getTitle());
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> update(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id, @RequestBody @Valid MovieRequest movieRequest) {
        log.info("Start to update a movie with id={}", id);

        MovieResponse movieResponse = movieService.update(id, movieRequest);

        log.info("Complete to update a movie with id={}", id);
        return new ResponseEntity<>(movieResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieResponse> get(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id) {
        log.info("Start to get a movie with id={}", id);

        MovieResponse movieResponse = movieService.get(id);

        log.info("Complete to get a movie with id={}", id);
        return new ResponseEntity<>(movieResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/movies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) Long id) {
        log.info("Start to delete a movie with id={}", id);

        movieService.delete(id);

        log.info("Complete to delete a movie with id={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v1/movies")
    public ResponseEntity<GroupMovieResponse> getList(@RequestParam(value = "page", defaultValue = "0", required = false) @Min(0) @Max(Constants.MAX_PAGE_NO) int page,
                                                      @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE_AS_STR, required = false) @Min(1) @Max(Constants.MAX_PAGE_SIZE) int size) {
        log.info("Start to get list movie with page={} and size={}", page, size);

        GroupMovieResponse groupMovieResponse = movieService.getList(page, size);

        log.info("Complete to get list movie with page={} and size={}", page, size);
        return new ResponseEntity<>(groupMovieResponse, HttpStatus.OK);
    }

}
