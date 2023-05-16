package com.example.vizsgaremek.game;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;


    @Transactional
    public GameDto createGame(CreateGameCommand command) {
        Game game = new Game(command.getName(),command.getReleaseDate(),command.getGenre());
        gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    public List<GameDto> getAllGames(Optional<String> gameTitle) {
        List<Game> games;
        if (gameTitle.isPresent()){
            games=gameRepository.findGameByNameContains(gameTitle.get());
        }else{
            games=gameRepository.findAll();
        }
        return gameMapper.toDto(games);
    }

    @Transactional
    public GameDto findGameById(long id) {
        Game game = findById(id);
        return gameMapper.toDto(game);
    }
    private Game findById(long id){
        Optional<Game> game = gameRepository.findById(id);
        if (game.isEmpty()){
            throw new GameNotFoundException(id);
        }
        return game.get();
    }

    @Transactional
    public GameDto updateGame(long id,UpdateMovieCommand command) {
        Game game = findById(id);
        game.setName(command.getName());
        game.setGenre(command.getGenre());
        game.setReleaseDate(command.getReleaseDate());
        return gameMapper.toDto(game);
    }

    public void deleteGame(long id) {
        gameRepository.deleteById(id);
    }

    public List<GameWithoutReviewsDto> getGamesWithHigherRatingThan(double rating) {
        List<Game> games = gameRepository.findGamesByAvgRatingGreaterThan(rating);
        if (games.size()==0){
            throw new GameNotFoundException(rating);
        }


       return gameMapper.toGameWithoutReviewDto(gameRepository.findGamesByAvgRatingGreaterThan(rating));
    }

    public List<GameWithoutReviewsDto> findGamesWithChosenGenre(Genre genre) {
        List<Game> games = gameRepository.findGamesByGenreEquals(genre);
        if (games.isEmpty()){
            throw new GameNotFoundException(genre);
        }
        return gameMapper.toGameWithoutReviewDto(games);
    }
}
