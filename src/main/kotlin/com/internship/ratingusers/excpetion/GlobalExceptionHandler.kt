package com.internship.ratingusers.excpetion

import com.google.firebase.auth.FirebaseAuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(FirebaseAuthException::class)
    fun handleFirebaseAuthException(ex: FirebaseAuthException): ResponseEntity<Any?> {
        val exceptionDetails = ExceptionDetails(HttpStatus.valueOf(ex.httpResponse.statusCode), ex.message.toString(),ex.cause.toString())
        return ResponseEntity<Any?>(exceptionDetails,exceptionDetails.status)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException, request: WebRequest): ResponseEntity<*>? {
        val exceptionDetails = ExceptionDetails(HttpStatus.UNAUTHORIZED, ex.message.toString(), request.getDescription(false))
        return ResponseEntity<Any>(exceptionDetails, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: Exception, request: WebRequest): ResponseEntity<*>? {
        val exceptionDetails = ExceptionDetails(HttpStatus.UNAUTHORIZED, ex.message.toString(), request.getDescription(false))
        return ResponseEntity<Any>(exceptionDetails, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<*>? {
        val exceptionDetails = ExceptionDetails(HttpStatus.BAD_REQUEST, ex.message.toString(), request.getDescription(false))
        return ResponseEntity<Any>(exceptionDetails, HttpStatus.BAD_REQUEST)
    }
}