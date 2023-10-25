package com.assessment.movie.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    @Size(min = 1, max = 200)
    private String category;

    @DecimalMax("5.0")
    @DecimalMin("0.5")
    @Digits(integer = 1, fraction = 1)
    @NotNull
    private BigDecimal starRating;
}
