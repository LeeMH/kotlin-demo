package me.project3.demo.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenUtil(
    @Value("\${app.jwt.secret}")
    val secret: String,

    @Value("\${app.jwt.token-valid-min}")
    val tokenValidMinutes: Int,

    @Value("\${app.jwt.refresh-token-valid-min}")
    val refreshTokenValidMinutes: Int
) {
    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }

    fun getSubject(token: String): String {
        return getAllClaimsFromToken(token).subject;
    }

    fun getExpiration(token: String): Date {
        return getAllClaimsFromToken(token).expiration
    }

    fun isExpired(token: String): Boolean {
        val expiration = getExpiration(token)
        return expiration.before(Date())
    }

    fun generateToken(id: Long, loginId: String, authorities: List<SimpleGrantedAuthority>): String {
        val jwtTokenValidity = (tokenValidMinutes * 60 * 1000).toLong()
        val claims = mutableMapOf<String, Any>()
        claims["id"] = id //id Long 타입!!
        claims["login_id"] = loginId //로그인 id
        claims["auth"] = authorities

        return generateToken(claims, loginId, jwtTokenValidity)
    }

    fun generateRefreshToken(id: Long, loginId: String, authorities: List<SimpleGrantedAuthority>): String {
        val jwtTokenValidity = (refreshTokenValidMinutes * 60 * 1000).toLong()
        val claims = mutableMapOf<String, Any>()
        claims["id"] = id //id Long 타입!!
        claims["login_id"] = loginId //로그인 id
        claims["auth"] = authorities

        return generateToken(claims, loginId, jwtTokenValidity)
    }

    private fun generateToken(claims: Map<String, Any?>, subject: String, ttl: Long): String {
        return Jwts.builder()
            .setSubject(subject)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + ttl))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

}
