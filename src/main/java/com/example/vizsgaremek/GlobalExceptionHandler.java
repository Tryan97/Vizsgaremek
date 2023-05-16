package com.example.vizsgaremek;

import com.example.vizsgaremek.game.GameNotFoundException;
import com.example.vizsgaremek.review.ReviewNotFoundException;
import com.example.vizsgaremek.review.UserAlreadyHasReviewException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ProblemDetail handleIllegalArgumentException(GameNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage());
        detail.setType(URI.create("games/game-not-found"));
        return detail;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("games/not-valid"));
        return detail;
    }
    @ExceptionHandler(ReviewNotFoundException.class)
    public ProblemDetail handleIllegalArgumentException(ReviewNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage());
        detail.setType(URI.create("reviews/review-not-found"));
        return detail;
    }
    @ExceptionHandler(UserAlreadyHasReviewException.class)
    public ProblemDetail handleIllegalArgumentException(UserAlreadyHasReviewException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("reviews/user-has-review-already"));
        return detail;
    }

}
