package com.example.vizsgaremek.game;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameDto toDto(Game game);
    List<GameDto> toDto(List<Game> games);

    GameWithoutReviewsDto toGameWithoutReviewDto(Game game);

    List<GameWithoutReviewsDto> toGameWithoutReviewDto(List<Game>games);
}
