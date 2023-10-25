package com.assessment.movie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {

    private Long id;

    private String title;

    private String category;

    private BigDecimal starRating;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
