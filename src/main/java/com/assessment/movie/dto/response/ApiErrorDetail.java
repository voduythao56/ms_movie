package com.assessment.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@Data
public class ApiErrorDetail {

    private String field;

    private String value;

    private String location;

    private String issue;
}
