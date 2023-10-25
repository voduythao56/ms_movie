package com.assessment.movie.exception;

import com.assessment.movie.dto.response.ApiError;
import com.assessment.movie.dto.response.ApiErrorDetail;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpMethod;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class ApiErrorBuilder {

    public static ApiError buildInvalidInputError(Exception exception, HttpServletRequest request) {
        List<ApiErrorDetail> apiErrorDetails = null;
        if (exception instanceof ConstraintViolationException) {
            apiErrorDetails = buildDetails((ConstraintViolationException) exception, request.getMethod());
        } else if (exception instanceof MethodArgumentNotValidException) {
            apiErrorDetails = buildDetails((MethodArgumentNotValidException) exception);
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            apiErrorDetails = buildDetails((MethodArgumentTypeMismatchException) exception);
        }
        return ApiError.builder()
                .code(ErrorCode.INVALID_INPUT.getCode())
                .message(ErrorCode.INVALID_INPUT.getDescription())
                .details(apiErrorDetails)
                .build();
    }

    public static ApiError buildHttpMethodNotSupported(HttpRequestMethodNotSupportedException exception){
        return ApiError.builder()
                .code(ErrorCode.HTTP_METHOD_NOT_ALLOW.getCode())
                .message(ErrorCode.HTTP_METHOD_NOT_ALLOW.getDescription())
                .details(buildDetails(exception))
                .build();
    }

    private static List<ApiErrorDetail> buildDetails(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        String type = Optional.ofNullable(exception.getRequiredType()).map(Class::getSimpleName).orElse(null);
        Object value = exception.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't",
                name, type, value);

        return Collections.singletonList(ApiErrorDetail.builder()
                .field(name)
                .value(String.valueOf(value))
                .issue(message)
                .build());
    }

    private static List<ApiErrorDetail> buildDetails(HttpRequestMethodNotSupportedException exception) {
        return Collections.singletonList(ApiErrorDetail.builder()
                .issue(exception.getMessage())
                .build());
    }


    private static List<ApiErrorDetail> buildDetails(ConstraintViolationException exception, String httpMethod) {
        return exception.getConstraintViolations().stream()
                .map(constraint -> ApiErrorDetail.builder()
                        .field(getField(constraint.getPropertyPath(), httpMethod))
                        .value(constraint.getInvalidValue().toString())
                        .issue(constraint.getMessage())
                        .build())
                .collect(Collectors.toList());
    }

    private static String getField(Path propertyPath, String httpMethod) {
        if (HttpMethod.GET.name().equals(httpMethod)) {
            return ((PathImpl) propertyPath).getLeafNode().getName();
        }
        return propertyPath.toString();
    }

    private static List<ApiErrorDetail> buildDetails(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .map(err -> ApiErrorDetail.builder()
                        .field(err instanceof FieldError ? ((FieldError) err).getField() : err.getObjectName())
                        .value(err instanceof FieldError ? String.format("%s", ((FieldError) err).getRejectedValue()) : null)
                        .issue(err.getDefaultMessage())
                        .build()).collect(Collectors.toList());
    }
}
