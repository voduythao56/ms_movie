package com.assessment.movie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMovieResponse {

    private int page;

    private int size;

    private List<MovieResponse> movies;
}
