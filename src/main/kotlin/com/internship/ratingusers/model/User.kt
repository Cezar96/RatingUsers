package com.internship.ratingusers.model

data class User(
        var uid :String,
        var email : String,
        var rating : Rating,
)