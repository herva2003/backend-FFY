package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.Review;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.ReviewDTO;
import com.puccampinas.backendp5noname.services.ReviewService;
import com.puccampinas.backendp5noname.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@SecurityRequirement(name = "bearer-key")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public Review createReview(@RequestBody ReviewDTO review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // Obtenha o objeto User autenticado
        String userId = user.getId(); // Extraia o ID do usuário
        System.out.println("UserID from context: " + userId); // Log do userId extraído do contexto

        Review newReview = reviewService.addReview(review);
        userService.addReviewIdToUser(userId, newReview.getId());
        return newReview;
    }

    @GetMapping("/")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable String id) {
        return reviewService.getReviewById(id);
    }

    @PutMapping("/{id}")
    public Review updateReview(@PathVariable String id, @RequestBody Review reviewDetails) {
        return reviewService.updateReview(id, reviewDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
    }
}