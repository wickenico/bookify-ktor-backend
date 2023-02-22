package com.nw.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTAuthenticationProvider

@Deprecated("we now use Basic Auth")
class JwtConfig(jwtSecret: String) {

    companion object Constants {
        // jwt config
        private const val jwtIssuer = "com.maaxgr"
        private const val jwtRealm = "com.maaxgr.todolist"

        // claims
        private const val CLAIM_USERID = "userId"
        private const val CLAIM_USERNAME = "userName"
        private const val CLAIM_EMAIL = "email"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for an authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .withClaim(CLAIM_EMAIL, user.email)
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userId = it.payload.getClaim(CLAIM_USERID).asInt()
            val userName = it.payload.getClaim(CLAIM_USERNAME).asString()
            val email = it.payload.getClaim(CLAIM_EMAIL).asString()

            if (userId != null && userName != null && email != null) {
                JwtUser(userId, userName, email)
            } else {
                null
            }
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(val userId: Int, val userName: String, val email: String) : Principal
}
