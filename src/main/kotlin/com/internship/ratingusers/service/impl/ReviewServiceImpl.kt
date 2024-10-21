package com.internship.ratingusers.service.impl

import com.google.firebase.database.*
import com.internship.ratingusers.model.Rating
import com.internship.ratingusers.model.Review
import com.internship.ratingusers.service.ReviewService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class ReviewServiceImpl : ReviewService {
    private val logger: Logger = LoggerFactory.getLogger(ReviewService::class.java)

    var userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    var reviewsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("reviews")
    override fun review(userId: String, review: Review) {
        review.reviewId = reviewsRef.push().key
        reviewsRef.child(userId).child(review.reviewId).setValue(review) { databaseError: DatabaseError?, databaseReference: DatabaseReference? ->
            if (databaseError != null) {
                logger.error("Data could not be saved: {}", databaseError.message)
            } else {
                addUserRating(userId, review.rating!!)
                logger.info("Review saved successfully at {}", databaseReference?.path)
            }
        }
    }

    override fun deleteReview(userId: String, reviewId: String) {
        val reviewToDeleteRef: DatabaseReference = reviewsRef.child(userId).child(reviewId)
        reviewToDeleteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val review: Review? = dataSnapshot.getValue(Review::class.java)
                if (review != null) {
                    subtractUserRating(userId, review.rating!!)
                    reviewToDeleteRef.removeValueAsync()
                    logger.info("Review deleted successfully for review ID: {}", reviewId)
                } else {
                    logger.warn("Review not found for user ID: {} and review ID: {}", userId, reviewId)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                logger.error("The read failed: {}", databaseError.message)
            }
        })
    }

    private fun addUserRating(userId: String, addRating: Int) {
        userRef.child(userId).child("rating").runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (currentData.value == null) {
                    return Transaction.success(currentData)
                }
                var rating = currentData.getValue(Rating::class.java)
                if (rating == null) {
                    rating = Rating(0, 0, 0.0)
                }
                rating.sum += addRating
                rating.count++
                rating.average = rating.sum.toDouble() / rating.count
                currentData.value = rating
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                if (committed) {
                    logger.info("User rating updated successfully for user ID: {}", userId)
                } else {
                    logger.error("User rating could not be updated: {}", error?.message)
                }
            }
        })
    }

    private fun subtractUserRating(userId: String, subtractRating: Int) {
        userRef.child(userId).child("rating").runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (currentData.value == null) {
                    return Transaction.success(currentData)
                }
                var rating = currentData.getValue(Rating::class.java)
                if (rating == null) {
                    rating = Rating(0, 0, 0.0)
                }
                rating.sum -= subtractRating
                rating.count--
                rating.average = rating.sum.toDouble() / rating.count
                currentData.value = rating
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                if (committed) {
                    logger.info("User rating updated successfully for user ID: {}", userId)
                } else {
                    logger.error("User rating could not be updated: {}", error?.message)
                }
            }
        })
    }
}