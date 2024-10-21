package com.internship.ratingusers.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityContext { securityContext: SecurityContextConfigurer<HttpSecurity?> -> securityContext.requireExplicitSave(false) }
                .sessionManagement { sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
                .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
                .cors { cors: CorsConfigurer<HttpSecurity> -> cors.disable() }
                .addFilterAfter(AuthenticationFilter(), BasicAuthenticationFilter::class.java)
                .authorizeHttpRequests { authorize ->
                    authorize
                            .requestMatchers("/register").permitAll()
                            .requestMatchers("/users/**").authenticated()
                            .anyRequest().authenticated()
                }

        return http.build()
    }
}