package com.internship.ratingusers.service.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.internship.ratingusers.model.Rating
import com.internship.ratingusers.model.User
import com.internship.ratingusers.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    override fun register(email: String?, password: String?) {
        val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
        try {
            val userRecord = FirebaseAuth.getInstance().createUser(request)
            writeToDatabase(userRecord)
        } catch (e: FirebaseAuthException) {
            throw FirebaseAuthException(e)
        }
    }

    override fun writeToDatabase(userRecord: UserRecord) {
        val user = User(userRecord.uid, userRecord.email, Rating(0, 0, 0.0))
        usersRef.child(userRecord.uid).setValue(user) { error: DatabaseError?, ref: DatabaseReference? -> println("User Registered") }
    }

    override fun assignRole(uid: String?, role: String?) {
        val claims: MutableMap<String, Any> = HashMap()
        claims["role"] = role!! // e.g., "admin", "user", "moderator"
        try {
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims)
        } catch (e: FirebaseAuthException) {
            throw RuntimeException(e)
        }
        println("Role assigned to user with UID: $uid")
    }
}