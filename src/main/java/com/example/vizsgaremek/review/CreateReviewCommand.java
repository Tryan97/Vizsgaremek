package com.example.vizsgaremek.review;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateReviewCommand {
    @NotBlank
    @Schema(description = "username of the person who writes the review",example = "JohnDoe")
    private String username;
    @Schema(description = "You can express your opinion here about the game",example = "There is place for improvement in the story.")
    private String text;
    @Min(value = 1)
    @Max(value = 5)
    @Schema(description = "You can rate the game from 1-5.",example = "3")
    private int rating;


}
