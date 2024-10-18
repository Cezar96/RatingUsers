package com.internship.ratingusers.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Value("\${firebase.admin.sdk.path}")
    private lateinit var firebaseSdkPath: String

    @Value("\${firebase.database.path}")
    private lateinit var firebaseDatabasePath: String

    @PostConstruct
    fun initializeFirebase(): FirebaseApp? {
        var firebaseApp: FirebaseApp? = null
        try {
            val serviceAccount = FileInputStream(firebaseSdkPath)

            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(firebaseDatabasePath)
                    .build()

            firebaseApp = FirebaseApp.initializeApp(options)
            println("Firebase initialized successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return firebaseApp
    }
}