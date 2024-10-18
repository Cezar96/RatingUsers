package com.internship.ratingusers.controller

import com.internship.ratingusers.model.RegistrationForm
import com.internship.ratingusers.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {

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
}