package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.jwtconfig.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtUserService {
    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var userDetailsService: JwtUserDetailsService

    fun createAuthenticationToken(user: User): String {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user.email)
        return "Bearer ${jwtTokenUtil.generateToken(userDetails)}"
    }
}