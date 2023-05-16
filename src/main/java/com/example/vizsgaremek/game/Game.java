package com.example.vizsgaremek.game;

import com.example.vizsgaremek.review.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},mappedBy = "game")
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "average_rating")
    private Double avgRating;

    public Game(String name, LocalDate releaseDate, Genre genre) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public void updateRating(){
        avgRating =(double)Math.round(reviews.stream().mapToDouble(d->d.getRating()).average().getAsDouble()*100.0)/100.0;
    }
}
