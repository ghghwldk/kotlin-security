package com.kotlinsecurity.template.jwt

import com.kotlinsecurity.template.controller.dto.TokenDto
import org.springframework.stereotype.Component
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.util.*
import java.util.stream.Collectors

//@Slf4j
@Component
class TokenProvider(@Value("\${jwt.secret}") secretKey: String) {

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "Bearer"
        private const val ACCESS_TOKEN_EXPIRE_TIME: Long = 1000 * 60 * 30 // 30 minutes
        private const val REFRESH_TOKEN_EXPIRE_TIME: Long = 1000 * 60 * 60 * 24 * 7 // 7 days
    }
    private val log = LoggerFactory.getLogger(javaClass)
    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun generateTokenDto(authentication: Authentication): TokenDto {
        // Get authorities
        val authorities = authentication.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","))

        val now = Date().time

        // Generate Access Token
        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        val accessToken = Jwts.builder()
                .setSubject(authentication.name) // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities) // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (example)
                .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
                .compact()

        // Generate Refresh Token
        val refreshToken = Jwts.builder()
                .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact()

        return TokenDto(
                grantType = BEARER_TYPE,
                accessToken = accessToken,
                accessTokenExpiresIn = accessTokenExpiresIn.time,
                refreshToken = refreshToken
        )
    }

    fun getAuthentication(accessToken: String?): Authentication {
        // Decrypt token
        val claims = parseClaims(accessToken)

        if (claims[AUTHORITIES_KEY] == null) {
            throw RuntimeException("Token does not contain any authority information.")
        }

        // Get authorities from claims
        val authorities = claims[AUTHORITIES_KEY].toString().split(",")
                .map { SimpleGrantedAuthority(it) }

        // Create UserDetails object and return Authentication
        val principal: UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            log.info("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            log.info("Malformed JWT token.")
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.")
        } catch (e: io.jsonwebtoken.UnsupportedJwtException) {
            log.info("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            log.info("Invalid JWT token.")
        }
        return false
    }

    private fun parseClaims(accessToken: String?): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}
