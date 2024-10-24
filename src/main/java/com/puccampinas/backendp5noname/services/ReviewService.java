package com.puccampinas.backendp5noname.services;

import com.puccampinas.backendp5noname.ResourceNotFoundException;
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
}