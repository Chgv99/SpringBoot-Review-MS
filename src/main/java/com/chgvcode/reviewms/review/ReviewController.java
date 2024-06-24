package com.chgvcode.reviewms.review;

import com.chgvcode.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    private ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewService.findAll(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createReview(@RequestParam Long companyId, @RequestBody Review review){
        if (reviewService.addReview(companyId, review)){
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not added", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId){
        return new ResponseEntity<>(reviewService.getReview(reviewId), HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review){
        if (reviewService.updateReview(reviewId, review))
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("Review not updated", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        if (reviewService.deleteReview(reviewId)){
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not deleted", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId){
        List<Review> reviewList = reviewService.findAll(companyId);
        return reviewList.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
}