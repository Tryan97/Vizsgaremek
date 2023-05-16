package com.example.vizsgaremek.review;

import jakarta.validation.constraints.NotBlank;

public class UserAlreadyHasReviewException extends RuntimeException {
    public UserAlreadyHasReviewException(long gameId, @NotBlank String username) {
        super(String.format("There is already a review for game with game id: %d and username : %s",gameId,username));
    }
}
