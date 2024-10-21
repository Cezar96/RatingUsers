package com.internship.ratingusers.config.security

import com.google.firebase.auth.FirebaseToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticationToken : AbstractAuthenticationToken {
    private var firebaseToken: FirebaseToken? = null

    constructor(
            firebaseToken: FirebaseToken?, authorities: List<GrantedAuthority?>?) : super(authorities) {
        this.firebaseToken = firebaseToken
    }

    constructor(authorities: Collection<GrantedAuthority?>?) : super(authorities)

    override fun getCredentials(): Any? {
        return null
    }


    override fun getPrincipal(): Any {
        return firebaseToken!!.uid
    }
}