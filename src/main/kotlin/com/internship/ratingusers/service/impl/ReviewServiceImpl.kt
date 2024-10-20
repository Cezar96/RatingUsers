package com.internship.ratingusers.service.impl

import com.google.firebase.database.*
import com.internship.ratingusers.model.Rating
import com.internship.ratingusers.model.Review
import com.internship.ratingusers.service.ReviewService
import org.springframework.stereotype.Service
import java.util.*


@Service
class ReviewServiceImpl : ReviewService {

    var userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    var reviewsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("reviews")
    override fun review(userId: String, review: Review) {
        review.reviewId = reviewsRef.push().key
        reviewsRef.child(userId).child(review.reviewId).setValue(review) { databaseError: DatabaseError?, databaseReference: DatabaseReference? ->
            if (Objects.nonNull(databaseError)) {
                println("Data could not be saved. ${databaseError?.message}")
            } else {
                addUserRating(userId, review.rating!!)
                println("Data saved successfully at ${databaseReference?.path}")
            }
        }
    }

    override fun deleteReview(userId: String, reviewId: String) {
        val reviewToDeleteRef: DatabaseReference = reviewsRef.child(userId).child(reviewId)
        reviewToDeleteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val review: Review? = dataSnapshot?.getValue(Review::class.java)
                subtractUserRating(userId, review?.rating!!)
                reviewToDeleteRef.removeValueAsync()
                println("Data deleted successfully")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    private fun addUserRating(userId: String, addRating: Int) {
        // Update the user's rating using a transaction
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
                    println("Data saved successfully")
                } else {
                    // Handle the error
                    println("Data could not be saved. ${error?.message}")
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
                    println("Data deleted successfully")
                } else {
                    // Handle the error
                    println("Data could not be saved. ${error?.message}")
                }
            }
        })
    }
}