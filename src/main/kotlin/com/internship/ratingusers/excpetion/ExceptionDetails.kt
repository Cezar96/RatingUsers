package com.internship.ratingusers.excpetion

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ExceptionDetails(val status: HttpStatus, val message: String, val details: String) {
    val timestamp: LocalDateTime = LocalDateTime.now()
}