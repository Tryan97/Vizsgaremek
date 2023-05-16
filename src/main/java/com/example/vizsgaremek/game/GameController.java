package com.example.vizsgaremek.game;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/games")
@AllArgsConstructor
@Tag(name = "Operations on games")
public class GameController {

    private GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a game")
    @ApiResponse(responseCode = "201",description = "Game has been created")
    @ApiResponse(responseCode = "406",description = "Failed on validation")
    public GameDto createGame(@Valid @RequestBody CreateGameCommand command){
        return gameService.createGame(command);
    }

    @GetMapping
    @Operation(summary = "Finds all of the games or the games that contain the game title part")
    @ApiResponse(responseCode = "200",description = "Game has been created")
    public List<GameDto> getAllGames(@RequestParam Optional<String> gameTitle){
        return gameService.getAllGames(gameTitle);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Finds a game by id")
    @ApiResponse(responseCode = "200",description = "Game has been found")
    @ApiResponse(responseCode = "404",description = "Game has not been found with given id")
    public GameDto findGameById(@PathVariable("id")long id){
        return gameService.findGameById(id);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Updates a game by id")
    @ApiResponse(responseCode = "201",description = "Game has been updated")
    @ApiResponse(responseCode = "406",description = "Failed on validation")
    public GameDto updateGame(@PathVariable("id")long id,@Valid @RequestBody UpdateMovieCommand command){
        return gameService.updateGame(id,command);

    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a game by id")
    @ApiResponse(responseCode = "204",description = "Game has been deleted")
    public void deleteGame(@PathVariable("id") long id){
        gameService.deleteGame(id);
    }
    @GetMapping("/ratings")
    @Operation(summary = "Find all the games with Higher rating than given")
    @ApiResponse(responseCode = "200",description = "Games have been found")
    @ApiResponse(responseCode = "404",description = "Games have not been found with greater ratings than given")
    public List<GameWithoutReviewsDto>getGamesWithHigherRatingThan(@RequestParam double rating){
        return gameService.getGamesWithHigherRatingThan(rating);
    }
    @GetMapping("/genres")
    @Operation(summary = "Find all games with chosen genre")
    @ApiResponse(responseCode = "200",description = "Games have been found")
    @ApiResponse(responseCode = "404",description = "Games haven not been found with this genre")
    public List<GameWithoutReviewsDto> findGamesWithChosenGenre (@RequestParam Genre genre){
        return gameService.findGamesWithChosenGenre(genre);
    }

}
