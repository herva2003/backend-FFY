package com.puccampinas.backendp5noname.services;

import com.puccampinas.backendp5noname.ResourceNotFoundException;
import com.puccampinas.backendp5noname.domain.Review;
import com.puccampinas.backendp5noname.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review getReviewById(String reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }
}