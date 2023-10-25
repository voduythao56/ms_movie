package com.assessment.movie.exception;


public class NoMovieFoundException extends BaseException {
    public NoMovieFoundException() {
        super(ErrorCode.NO_MOVIE_FOUND);
    }
}
