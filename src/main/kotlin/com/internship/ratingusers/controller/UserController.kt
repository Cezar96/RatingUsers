package com.internship.ratingusers.controller

import com.internship.ratingusers.model.RegistrationForm
import com.internship.ratingusers.model.Review
import com.internship.ratingusers.service.ReviewService
import com.internship.ratingusers.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService,
                     private val reviewService: ReviewService) {

    @PostMapping(value = ["/users"])
    fun register(@RequestBody registrationForm: RegistrationForm): ResponseEntity<*>? {
        userService.register(registrationForm.email, registrationForm.password)
        return ResponseEntity<Any>(HttpStatus.CREATED)
    }


    @PostMapping(value = ["/users/{userId}"])
    fun assignRole(@PathVariable userId: String?, @RequestParam role: String?): ResponseEntity<*>? {
        userService.assignRole(userId, role)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping(value = ["/users/{userId}/reviews"])
    fun review(@PathVariable userId: String, @RequestBody review: Review): ResponseEntity<*>? {
        reviewService.review(userId, review)
        return ResponseEntity<Any>(HttpStatus.OK)
    }
    @DeleteMapping(value = ["/users/{userId}/reviews/{reviewId}"])
    fun removeReview(@PathVariable userId: String,@PathVariable reviewId:String): ResponseEntity<*>? {
        reviewService.deleteReview(userId, reviewId)
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}