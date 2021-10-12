package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class JwtUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userService: UserService

    override fun loadUserByUsername(username: String): User {
        val user = userService.findByEmail(username)
        return User(user.email, user.password, emptyList())
    }
}