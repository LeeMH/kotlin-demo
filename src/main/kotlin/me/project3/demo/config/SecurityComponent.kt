package me.project3.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import me.project3.demo.common.inout.AppResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.Serializable
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    val jwtUserDetailsService: UserDetailsService,
    val jwtTokenUtil: JwtTokenUtil
) : OncePerRequestFilter() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        val mapper = ObjectMapper()
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val requestTokenHeader = request.getHeader("Authorization") ?: ""

            if (requestTokenHeader.isNotEmpty()) {
                val claims = jwtTokenUtil.getAllClaimsFromToken(requestTokenHeader)

                if (claims != null && SecurityContextHolder.getContext().authentication == null) {
                    val authorities = (claims["auth"] as List<Map<String, String>>)
                        .map { SimpleGrantedAuthority(it["authority"]) }

                    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(claims.subject, requestTokenHeader, authorities)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            val appResponse = AppResponse.error("인증이 만되었거나 세션이 유효하지 않습니다.")
            response.status = HttpStatus.NON_AUTHORITATIVE_INFORMATION.value()
            response.contentType = "application/json"
            response.characterEncoding = "utf-8"
            response.writer.write(mapper.writeValueAsString(appResponse))
        }
    }
}

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}

@Component
class MyUserDetailService: UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }

}
