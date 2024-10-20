package com.internship.ratingusers.model

import java.time.Instant

data class Review(
         var reviewId: String? = null, // Unique ID for the review
         val fromUserId: String? = null, // The user who wrote the review
         val rating: Int? = null, // Rating score (e.g., 1-5)
         val comment: String? = null, // Review comment
         val timestamp: Long? = Instant.now().epochSecond
)


