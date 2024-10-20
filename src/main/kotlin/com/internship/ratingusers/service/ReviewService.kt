package com.internship.ratingusers.service

import com.internship.ratingusers.model.Review

interface ReviewService {
    fun review(userId: String, review: Review)

    fun deleteReview(userId: String, reviewId: String)
}