package com.example.vizsgaremek.game;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(long id) {
        super(String.format("Game not found with id %d",id));
    }
    public GameNotFoundException(double rating) {
        super(String.format("Games not found with greater rating than %f",rating));
    }
    public GameNotFoundException(Genre genre){
        super(String.format("Games not found with this genre: %s",genre));
    }


}
