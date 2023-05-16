package com.example.vizsgaremek.review;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDto(Review review);

    List<ReviewDto> toDto(List<Review> reviews);

    ReviewWithoutGameDto toReviewWithoutGameDto(Review review);
    List<ReviewWithoutGameDto> toReviewWithoutGameDto(List<Review> reviews);

}
