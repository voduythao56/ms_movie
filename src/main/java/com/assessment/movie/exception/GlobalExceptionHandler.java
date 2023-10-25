package com.assessment.movie.exception;

import com.assessment.movie.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleInvalidInput(
            Exception exception, HttpServletRequest request) {

        return new ResponseEntity<>(ApiErrorBuilder.buildInvalidInputError(exception, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoMovieFoundException.class)
    public ResponseEntity<ApiError> handleNoMovieFoundException(
            BaseException exception, HttpServletRequest request) {

        return new ResponseEntity<>(ApiError.builder()
                .code(exception.getErrorCode().getCode())
                .message(exception.getErrorCode().getDescription())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpMethodNotSupported(
            HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {

        return new ResponseEntity<>(ApiErrorBuilder.buildHttpMethodNotSupported(exception), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> handleCommonException(
            Exception exception, HttpServletRequest request) {

        return new ResponseEntity<>(ApiError.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
