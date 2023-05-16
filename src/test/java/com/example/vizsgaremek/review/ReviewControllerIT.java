package com.example.vizsgaremek.review;

import com.example.vizsgaremek.game.*;
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
class ReviewControllerIT {

    GameDto gameDto;
    @Autowired
    WebTestClient client;

    @Autowired
    ReviewService reviewService;

    @Autowired
    GameService gameService;

    @BeforeEach
    void init(){
        gameDto=client.post()
                .uri("/api/games")
                .bodyValue(new CreateGameCommand("Minecraft", LocalDate.of(2014,2,15), Genre.SANDBOX))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(GameDto.class).returnResult().getResponseBody();
    }
    @Test
    void createReview(){
        ReviewDto reviewDto = client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();

        assertEquals("Jonny",reviewDto.getUsername());
        assertNotNull(reviewDto.getId());
    }
    @Test
    void testGetReviewListForGame(){
        ReviewDto reviewDto =client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();
        client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jack","It is the best game I have ever played.",5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();

        List<ReviewWithoutGameDto> reviews =reviewService.listReviews(reviewDto.getGame().getId());
        assertEquals(2,reviews.size());
    }
    @Test
    void testUpdateReview(){
        ReviewDto reviewDto =client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();
        client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jack","It is the best game I have ever played.",5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();

        ReviewDto updated = client.put()
                .uri("/api/reviews/{id}/{username}",gameDto.getId(),reviewDto.getUsername())
                .bodyValue(new UpdateReviewCommand("It was alright",4))
                .exchange()
                .expectBody(ReviewDto.class).returnResult().getResponseBody();

        assertEquals(4,updated.getRating());
        assertEquals("It was alright",updated.getText());
    }
    @Test
    void testUsernameNotFound(){
        ProblemDetail detail = client.put()
                .uri("/api/reviews/{id}/{username}",gameDto.getId(),"Jane")
                .bodyValue(new UpdateReviewCommand("It was alright",4))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("reviews/review-not-found"),detail.getType());
    }
    @Test
    void testDeleteReview(){
        ReviewDto reviewDto =client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();
        reviewService.deleteReview(reviewDto.getId());
        ProblemDetail detail = client.put()
                .uri("/api/reviews/{id}/{username}",gameDto.getId(),reviewDto.getUsername())
                .bodyValue(new UpdateReviewCommand("It was alright",4))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("reviews/review-not-found"),detail.getType());
    }
    @Test
    void testCreateReviewWithWrongRating(){
        ProblemDetail detail = client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("John","Meh",0))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/not-valid"),detail.getType());
    }
    @Test
    void testCreateReviewWithWrongGameId(){
        ProblemDetail detail = client.post()
                .uri("/api/reviews/{gameid}",50)
                .bodyValue(new CreateReviewCommand("Jane","It was alright",4))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();
    assertEquals(URI.create("games/game-not-found"),detail.getType());
    }
    @Test
    void testGetReviewListForGameWithWrongGameId(){
        ProblemDetail detail = client.get()
                .uri("/api/reviews/{id}",50)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("games/game-not-found"),detail.getType());

    }
    @Test
    void testCreateReviewWithAlreadyExistingUsername(){
                 client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();
        ProblemDetail detail = client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("reviews/user-has-review-already"),detail.getType());
    }
    @Test
    void testDeleteReviewsByGameId(){
         client.post()
                .uri("/api/reviews/{id}",gameDto.getId())
                .bodyValue(new CreateReviewCommand("Jonny","It was horrible",2))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();

        client.delete()
                .uri("/api/reviews/{id}",gameDto.getId())
                .exchange()
                .expectStatus().isEqualTo(204)
                .expectBody(ReviewDto.class).returnResult().getResponseBody();



        assertEquals(0,gameDto.getReviews().size());
        assertNull(gameDto.getAvgRating());
    }




}