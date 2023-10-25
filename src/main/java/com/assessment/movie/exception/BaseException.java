package com.assessment.movie.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
