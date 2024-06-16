package com.chgvcode.reviewms.review.impl;

import com.chgvcode.reviewms.review.Review;
import com.chgvcode.reviewms.review.ReviewRepository;
import com.chgvcode.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> findAll(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review newReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setName(newReview.getName());
            review.setDescription(newReview.getDescription());
            review.setRating(newReview.getRating());
            reviewRepository.save(review);
            return true;
        }
        return false;

        /*List<Review> reviews = reviewRepository.findByCompanyId(companyId);

        for (Review review : reviews) {
            if (review.getId().equals(reviewId)){
                review.setName(newReview.getName());
                review.setDescription(newReview.getDescription());
                review.setRating(newReview.getRating());
                //review.setCompany(newReview);
                reviewRepository.save(review);
                return true;
            }
        }
        return false;*/
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review != null){
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }

}
