package com.example.vizsgaremek.game;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findGameByNameContains(String part);
    List<Game> findGamesByAvgRatingGreaterThan(double rating);

    List<Game>findGamesByGenreEquals(Genre genre);
}
