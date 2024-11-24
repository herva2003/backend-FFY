package com.puccampinas.backendp5noname.services;

import com.puccampinas.backendp5noname.ResourceNotFoundException;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.Review;
import com.puccampinas.backendp5noname.dtos.ReviewDTO;
import com.puccampinas.backendp5noname.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(ReviewDTO review) {
        return reviewRepository.save(new Review(review));
    }

    public List<Review> getReviewsByRecipeId(String recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
    }

    public Review updateReview(String id, Review reviewDetails) {
        Review review = getReviewById(id);

        review.setTitle(reviewDetails.getTitle());
        review.setDescription(reviewDetails.getDescription());
        review.setRating(reviewDetails.getRating());

        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }

    public double ratingReviewsByRecipeId(String recipeId) {
        List<Review> reviews = reviewRepository.findByRecipeId(recipeId);
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for recipe with id " + recipeId);
        }
        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        return averageRating;
    }
}