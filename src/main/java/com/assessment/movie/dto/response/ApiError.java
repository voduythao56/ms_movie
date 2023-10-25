package com.assessment.movie.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@Data
public class ApiError {

    private String code;

    private String message;

    private List<ApiErrorDetail> details;
}
