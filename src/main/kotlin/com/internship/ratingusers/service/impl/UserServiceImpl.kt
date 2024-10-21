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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    override fun register(email: String?, password: String?) {
        val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
        try {
            val userRecord = FirebaseAuth.getInstance().createUser(request)
            logger.info("User created with UID: {}", userRecord.uid)
            persistUserToDatabase(userRecord)
        } catch (e: FirebaseAuthException) {
            logger.error("Error creating user: {}", e.message)
            throw FirebaseAuthException(e)
        }
    }

    override fun persistUserToDatabase(userRecord: UserRecord) {
        val user = User(userRecord.uid, userRecord.email, Rating(0, 0, 0.0))
        usersRef.child(userRecord.uid).setValue(user) { error: DatabaseError?, ref: DatabaseReference? ->
            if (error != null) {
                logger.error("Error saving user to database: {}", error.message)
            } else {
                logger.info("User successfully saved to database with UID: {}", userRecord.uid)
            }
        }
    }

    override fun assignRole(uid: String?, role: String?) {
        val claims: MutableMap<String, Any> = HashMap()
        claims["role"] = role!!
        try {
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims)
            logger.info("Role '{}' assigned to user with UID: {}", role, uid)
        } catch (e: FirebaseAuthException) {
            logger.error("Error assigning role to user: {}", e.message)
            throw FirebaseAuthException(e)
        }
    }
}