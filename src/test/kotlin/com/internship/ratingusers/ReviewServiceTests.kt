package com.internship.ratingusers

import com.google.firebase.database.*
import com.internship.ratingusers.model.Review
import com.internship.ratingusers.service.impl.ReviewServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
@SpringBootTest
class ReviewServiceTest {

    private lateinit var reviewService: ReviewServiceImpl

    @Mock
    private lateinit var mockFirebaseDatabase: FirebaseDatabase
    @Mock
    private lateinit var mockReviewsRef: DatabaseReference

    @Mock
    private lateinit var mockUserRef: DatabaseReference

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Mock FirebaseDatabase references
        `when`(mockFirebaseDatabase.getReference("users")).thenReturn(mockUserRef)
        `when`(mockFirebaseDatabase.getReference("reviews")).thenReturn(mockReviewsRef)

        // Initialize ReviewService with the mocked FirebaseDatabase

        `when`(mockReviewsRef.child(anyString())).thenReturn(mockReviewsRef)
        `when`(mockUserRef.child(anyString())).thenReturn(mockUserRef)

        // Initialize ReviewService with mocked references
        reviewService = spy(ReviewServiceImpl(mockFirebaseDatabase).apply {
            reviewsRef = mockReviewsRef
            userRef = mockUserRef
        })
    }


    @Test
    fun `should save review and call addUserRating`() {
        val userId = "user123"
        val review = Review(rating = 5).apply { reviewId = "review456" }

        // Mock the push() to return the mockReviewsRef
        `when`(mockReviewsRef.push()).thenReturn(mockReviewsRef)
        `when`(mockReviewsRef.key).thenReturn(review.reviewId)

        // Mock the child() calls
        val mockUserRefChild = mock(DatabaseReference::class.java)
        val mockReviewRefChild = mock(DatabaseReference::class.java)

        // Chain the mock child() calls
        `when`(mockReviewsRef.child(userId)).thenReturn(mockUserRefChild)
        `when`(mockUserRefChild.child(review.reviewId)).thenReturn(mockReviewRefChild)

        // Mock the setValue to call the callback
        doAnswer { invocation ->
            val callback = invocation.getArgument<DatabaseReference.CompletionListener>(1)
            callback.onComplete(null, mock(DatabaseReference::class.java)) // Simulate success
            null
        }.`when`(mockReviewRefChild).setValue(any(), any())

        // Act
        reviewService.review(userId, review)

        // Verify saving behavior
        verify(mockReviewsRef).child(userId)
        verify(mockUserRefChild).child(review.reviewId)
        verify(reviewService).addUserRating(userId, review.rating!!)
    }

    @Test
    fun `should delete review and call subtractUserRating`() {
        val userId = "user123"
        val reviewId = "review456"
        val review = Review(rating = 5)

        // Mock behavior for retrieving the review
        val dataSnapshot = mock(DataSnapshot::class.java)
        `when`(dataSnapshot.getValue(Review::class.java)).thenReturn(review)

        // Mock the child reference
        val reviewRef = mock(DatabaseReference::class.java)
        `when`(mockReviewsRef.child(userId).child(reviewId)).thenReturn(reviewRef)

        // Mock the addValueEventListener
        doAnswer { invocation ->
            val listener = invocation.getArgument<ValueEventListener>(0)
            listener.onDataChange(dataSnapshot) // Simulate data retrieval
            null // Return Unit (Kotlin's void equivalent)
        }.`when`(reviewRef).addValueEventListener(any())

        // Act
        reviewService.deleteReview(userId, reviewId)

        // Verify that the subtractUserRating method was called
        verify(reviewService).subtractUserRating(userId, review.rating!!)
        verify(reviewRef).removeValueAsync()
    }

    @Test
    fun `should add user rating correctly`() {
        val userId = "user123"
        val addRating = 5

        // Mock the transaction behavior
        `when`(mockUserRef.child(userId).child("rating")).thenReturn(mockUserRef)

        // Simulate successful transaction completion
        doAnswer {
            val handler = it.getArgument<Transaction.Handler>(0)
            handler.onComplete(null, true, mock(DataSnapshot::class.java))
        }.`when`(mockUserRef).runTransaction(any())

        // Act
        reviewService.addUserRating(userId, addRating)

        // Verify transaction execution
        verify(mockUserRef).runTransaction(any())
    }

    @Test
    fun `should subtract user rating correctly`() {
        val userId = "user123"
        val subtractRating = 3

        // Mock the transaction behavior
        `when`(mockUserRef.child(userId).child("rating")).thenReturn(mockUserRef)

        // Simulate successful transaction completion
        doAnswer {
            val handler = it.getArgument<Transaction.Handler>(0)
            handler.onComplete(null, true, mock(DataSnapshot::class.java))
        }.`when`(mockUserRef).runTransaction(any())

        // Act
        reviewService.subtractUserRating(userId, subtractRating)

        // Verify transaction execution
        verify(mockUserRef).runTransaction(any())
    }
}