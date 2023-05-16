package com.example.vizsgaremek.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateMovieCommand {
    @NotBlank
    @Schema(description ="Name of the game",example = "Dota2")
    private String name;
    @PastOrPresent
    @Schema(description = "Release date of the game",example ="2015-11-11")
    private LocalDate releaseDate;

    @NotNull
    @Schema(description = "Genre of the game",example = "ACTION")
    private Genre genre;
}
