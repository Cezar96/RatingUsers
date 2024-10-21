package com.internship.ratingusers.service

import com.google.firebase.auth.UserRecord

interface UserService {
    fun register(email: String?, password: String?)

    fun persistUserToDatabase(userRecord: UserRecord)

    fun assignRole(uid: String?, role: String?)
}