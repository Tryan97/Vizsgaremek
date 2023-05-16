package com.example.vizsgaremek.game;

import com.example.vizsgaremek.review.CreateReviewCommand;
import com.example.vizsgaremek.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from reviews","delete from games"})
class GameControllerIT {

    GameDto gameDto;
    @Autowired
    WebTestClient client;

    @Autowired
    GameService service;

    @Autowired
    ReviewService reviewService;
    @BeforeEach
    void init(){
        gameDto=client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("Minecraft", LocalDate.of(2014,2,15),Genre.SANDBOX))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(GameDto.class).returnResult().getResponseBody();
    }

    @Test
    void testCreateGame(){
        assertEquals("Minecraft",gameDto.getName());
        assertNotNull(gameDto.getId());
    }

    @Test
    void testFindAllGames(){
        client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("Fifa23", LocalDate.of(2022,10,20),Genre.SPORTS))
                .exchange();

        List<GameDto> resultList = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games").build())
                .exchange()
                .expectBodyList(GameDto.class).returnResult().getResponseBody();

        assertEquals(2,resultList.size());
        assertEquals(Genre.SANDBOX,gameDto.getGenre());

    }
    @Test
    void testFindGameByPart(){
        client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("Fifa23", LocalDate.of(2022,10,20),Genre.SPORTS))
                .exchange();

        List<GameDto> resultList = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games").queryParam("gameTitle","Min").build())
                .exchange()
                .expectBodyList(GameDto.class).returnResult().getResponseBody();

        assertEquals(1,resultList.size());
        assertEquals(Genre.SANDBOX,gameDto.getGenre());
    }
    @Test
    void testGameNotFound(){
        ProblemDetail detail = client.get()
                .uri("/api/games/{id}",2)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/game-not-found"),detail.getType());
    }
    @Test
    void testFindGameById(){

        GameDto result =service.findGameById(gameDto.getId());

        assertNotNull(result.getId());
    }

    @Test
    void testUpdateGame(){
        GameDto updated = client.put()
                .uri("/api/games/{id}",gameDto.getId())
                .bodyValue(new UpdateMovieCommand("Fifa23",LocalDate.of(2022,11,15),Genre.SPORTS))
                .exchange()
                .expectBody(GameDto.class).returnResult().getResponseBody();

        assertEquals(updated.getId(),gameDto.getId());
        assertEquals("Fifa23",updated.getName());
    }
    @Test
    void testCreateGameWithEmptyName(){
        ProblemDetail detail = client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("",LocalDate.of(2015,4,6),Genre.RTS))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/not-valid"),detail.getType());
    }
    @Test
    void testCreateGameWithWrongDate(){
        ProblemDetail detail = client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("League of Legends",LocalDate.of(2024,4,6),Genre.RTS))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/not-valid"),detail.getType());
    }
    @Test
    void testDeleteGame(){
        service.deleteGame(gameDto.getId());
        ProblemDetail detail = client.get()
                .uri("/api/games/{id}",gameDto.getId())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();
        assertEquals(URI.create("games/game-not-found"),detail.getType());
    }
    @Test
    void testGetGamesWithHigherRatingThan(){
        client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jack","Not bad",3))
                .exchange();
        List<GameDto> resultList = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games/ratings").queryParam("rating",3).build())
                .exchange()
                .expectBodyList(GameDto.class).returnResult().getResponseBody();

        assertEquals(1,resultList.size());
        assertEquals(Genre.SANDBOX,gameDto.getGenre());
    }
    @Test
    void testGetGamesWithHigherRatingWithEmptyReturn(){
        client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jack","Not bad",3))
                .exchange();

        ProblemDetail detail = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games/ratings").queryParam("rating",5).build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/game-not-found"),detail.getType());

    }
    @Test
    void testFindGamesWithChosenGenre(){
        List<GameWithoutReviewsDto>games = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games/genres").queryParam("genre",Genre.SANDBOX).build())
                .exchange()
                .expectBodyList(GameWithoutReviewsDto.class).returnResult().getResponseBody();

        assertEquals(1,games.size());
        assertEquals("Minecraft",games.get(0).getName());
    }

    @Test
    void testGamesNotFoundWithChosenGenre(){
        ProblemDetail detail = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/games/genres").queryParam("genre",Genre.MOBA).build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/game-not-found"),detail.getType());
    }


}