package com.internship.ratingusers.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Value("\${firebase.admin.sdk.path}")
    private lateinit var firebaseSdkPath: String

    @Value("\${firebase.database.path}")
    private lateinit var firebaseDatabasePath: String


    @PostConstruct
    fun initializeFirebase() {
        val serviceAccount = FileInputStream(firebaseSdkPath)

        val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseDatabasePath)
                .build()

        FirebaseApp.initializeApp(options)
        println("Firebase initialized successfully.")
    }

    @Bean
    fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}