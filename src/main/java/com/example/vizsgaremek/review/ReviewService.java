package com.example.vizsgaremek.review;

import com.example.vizsgaremek.game.Game;
import com.example.vizsgaremek.game.GameNotFoundException;
import com.example.vizsgaremek.game.GameRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
    private GameRepository gameRepository;
    private ReviewMapper reviewMapper;


    @Transactional
    public ReviewDto createReview(long gameId, CreateReviewCommand command) {
        Optional<Game> game = gameRepository.findById(gameId);
        checkIfUsernameExistsForReview(gameId,command);
        if (game.isEmpty()){
            throw new GameNotFoundException(gameId);
        }
        Review review = new Review(command.getUsername(),command.getText(),command.getRating(),game.get());
        reviewRepository.save(review);
        game.get().updateRating();
        return  reviewMapper.toDto(review);
    }
    private void checkIfUsernameExistsForReview(long gameId,CreateReviewCommand command){
        Optional<Review> review = reviewRepository.findReviewByUsername(gameId,command.getUsername());
        if (review.isPresent()){
            throw new UserAlreadyHasReviewException(gameId,command.getUsername());
        }

    }

    public List<ReviewWithoutGameDto> listReviews(long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        List<Review> reviews = CheckForExceptions(gameId, game);
        log.info("Reviews: {}", reviews);
        return reviewMapper.toReviewWithoutGameDto(reviews);
    }

    private List<Review> CheckForExceptions(long gameId, Optional<Game> game) {
        if (game.isEmpty()){
            throw new GameNotFoundException(gameId);
        }
        List<Review> reviews = reviewRepository.findByGameId(gameId);
        if (reviews.size()==0){
            throw new ReviewNotFoundException(gameId);
        }
        return reviews;
    }

    @Transactional
    public ReviewDto updateReview(long gameId, String username, UpdateReviewCommand command) {
        Optional<Review> reviewOpt = reviewRepository.findReviewByUsername(gameId,username);
        if (reviewOpt.isEmpty()){
            throw new ReviewNotFoundException(gameId,username);
        }
        Review review = reviewOpt.get();
        review.setText(command.getText());
        review.setRating(command.getRating());
        review.getGame().updateRating();

        return reviewMapper.toDto(review);
    }

    public void deleteReview(long id) {
        reviewRepository.deleteById(id);
    }

    @Transactional
    public void deleteReviewsByGameId(long id) {
        Optional<Game> gameOpt = gameRepository.findById(id);
        if (gameOpt.isEmpty()){
            throw new GameNotFoundException(id);
        }
        reviewRepository.deleteReviewsByGame_Id(id);
        gameOpt.get().setReviews(new ArrayList<>());
        gameOpt.get().setAvgRating(null);
    }
}
