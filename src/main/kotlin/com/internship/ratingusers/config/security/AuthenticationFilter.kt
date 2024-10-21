package com.internship.ratingusers.config.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class AuthenticationFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                // Verify Firebase ID token
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val uid = decodedToken.uid
                // Extract the role from the custom claims
                val role = decodedToken.claims["role"] as String?

                // Set the user's role in the Spring Security principal
                val authorities = getAuthoritiesFromToken(decodedToken)
                SecurityContextHolder.getContext().authentication = AuthenticationToken(decodedToken, authorities)
                SecurityContextHolder.getContext().authentication.isAuthenticated = true
            } catch (e: Exception) {
                println("Invalid Firebase token: " + e.message)
            }
        }
        chain.doFilter(request, response)
    }

    companion object {
        private fun getAuthoritiesFromToken(token: FirebaseToken): List<GrantedAuthority> {
            var authorities = AuthorityUtils.NO_AUTHORITIES
            val claims = token.claims["role"]
            if (claims == null) {
                return authorities;
            } else {
                val permissions: MutableList<String?> = ArrayList()
                permissions.add(claims as String?)
                if (!permissions.isEmpty()) {
                    authorities = AuthorityUtils.createAuthorityList(permissions)
                }
            }
            return authorities
        }
    }
}