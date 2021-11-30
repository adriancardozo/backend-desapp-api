package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.jwt.TokenUtilJwt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtUserService {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var tokenUtilJwt: TokenUtilJwt

    @Autowired
    private lateinit var userDetailsService: JwtUserDetailsService

    fun createAuthenticationToken(user: User): String {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user.email)
        return "Bearer ${tokenUtilJwt.generateToken(userDetails)}"
    }

    fun userEmail(token: String): String = tokenUtilJwt.getUsernameFromToken(token.removePrefix("Bearer "))
}