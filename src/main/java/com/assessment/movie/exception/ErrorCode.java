package com.assessment.movie.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_INPUT("MV000001", "Invalid input data"),
    NO_MOVIE_FOUND("MV000002", "No movie found"),
    HTTP_METHOD_NOT_ALLOW("MV000003", "Method not allow"),
    INTERNAL_SERVER_ERROR("MV000004", "System error");

    private final String code;
    private final String description;
}
