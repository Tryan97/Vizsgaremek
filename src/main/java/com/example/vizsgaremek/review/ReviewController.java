package com.example.vizsgaremek.review;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
@Tag(name = "Managing reviews")
public class ReviewController {
    private ReviewService reviewService;

    @PostMapping("/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a review by game id")
    @ApiResponse(responseCode = "201",description = "Review has been created")
    @ApiResponse(responseCode = "406",description = "Failed on validation")
    @ApiResponse(responseCode = "404",description = "Game has not been found with given id")
    @ApiResponse(responseCode = "406",description = "There is already a review for this game with this username")
    public ReviewDto createReview(@PathVariable("gameId") long gameId, @Valid@RequestBody CreateReviewCommand command){
        return reviewService.createReview(gameId,command);
    }
    @GetMapping("/{gameId}")
    @Operation(summary = "Finds reviews by game id")
    @ApiResponse(responseCode = "200",description = "Reviews have been found")
    @ApiResponse(responseCode = "404",description = "Game has not been found with given id")
    @ApiResponse(responseCode = "404",description = "There is no review for the game yet")
    public List<ReviewWithoutGameDto> listReviews(@PathVariable("gameId") long gameId){
        return reviewService.listReviews(gameId);
    }
    @PutMapping("/{gameId}/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Updates a review by game id and username")
    @ApiResponse(responseCode = "201",description = "Review has been updated")
    @ApiResponse(responseCode = "404",description = "Review has not been found with given game id or incorrect username")
    public ReviewDto updateReview(@PathVariable("gameId")long gameId,@PathVariable("username")String username, @Valid @RequestBody UpdateReviewCommand command){
        return reviewService.updateReview(gameId,username,command);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a review by id")
    @ApiResponse(responseCode = "204",description = "Review has been deleted")
    public void deleteReview(@PathVariable("id") long id){
        reviewService.deleteReview(id);
    }

    @DeleteMapping("games/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes all the reviews by game id")
    @ApiResponse(responseCode = "404",description = "Game has not been found with given id")
    @ApiResponse(responseCode = "204",description = "Reviews have been deleted")
    public void deleteReviewsByGameId(@PathVariable("id")long id){
        reviewService.deleteReviewsByGameId(id);
    }

}
