package com.example.vizsgaremek.review;

import com.example.vizsgaremek.game.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String text;

    private int rating;
    @ManyToOne
    private Game game;

    public Review(String username, String text, int rating, Game game) {
        this.username = username;
        this.text = text;
        this.rating = rating;
        this.game = game;
    }

}
