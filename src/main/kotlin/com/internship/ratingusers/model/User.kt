package com.internship.ratingusers.model

data class User(
        var UUID :String,
        var email : String,
        var rating : Rating,
)