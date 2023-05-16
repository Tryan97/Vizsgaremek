package com.example.vizsgaremek.review;


import com.example.vizsgaremek.game.GameDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
    private Long id;

    private String username;
    private String text;

    private int rating;

    private GameDto game;

}
