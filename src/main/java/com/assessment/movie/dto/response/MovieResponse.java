package com.assessment.movie.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovieResponse {

    private Long id;

    private String title;

    private String category;

    private BigDecimal starRating;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
