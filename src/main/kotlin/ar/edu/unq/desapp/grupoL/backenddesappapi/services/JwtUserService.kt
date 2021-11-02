package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.configuration.jwtconfig.JwtTokenUtil
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.LoginUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class JwtUserService {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var userDetailsService: JwtUserDetailsService

    fun createAuthenticationToken(user: User): String {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user.email)
        return "Bearer ${jwtTokenUtil.generateToken(userDetails)}"
    }

    fun userEmail(token: String): String = jwtTokenUtil.getUsernameFromToken(token.removePrefix("Bearer "))
}