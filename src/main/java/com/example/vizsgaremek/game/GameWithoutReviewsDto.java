package com.example.vizsgaremek.game;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameWithoutReviewsDto {
    private Long id;
    private String name;

    private LocalDate releaseDate;

    private Genre genre;
    private Double avgRating;
}
