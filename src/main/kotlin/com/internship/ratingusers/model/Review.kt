package com.internship.ratingusers.model

import java.time.Instant

data class Review(
         var reviewId: String? = null,
         val fromUserId: String? = null,
         val rating: Int? = null,
         val comment: String? = null,
         val timestamp: Long? = Instant.now().epochSecond
)


