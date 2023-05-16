package com.example.vizsgaremek.review;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(long id, String username) {
        super(String.format("Review not found with id: %d and username %s ",id,username));
    }
    public ReviewNotFoundException(long id){
        super(String.format("There is no review for game id: %d",id));
    }
}
