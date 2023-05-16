package com.example.vizsgaremek.review;

import lombok.Data;

@Data
public class ReviewWithoutGameDto {
    private Long id;

    private String username;
    private String text;

    private int rating;
}
