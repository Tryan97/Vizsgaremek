package com.example.vizsgaremek.game;

import com.example.vizsgaremek.review.ReviewWithoutGameDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDto {

    private Long id;
    private String name;

    private LocalDate releaseDate;

    private Genre genre;
    private List<ReviewWithoutGameDto>reviews;
    private Double avgRating;


}
