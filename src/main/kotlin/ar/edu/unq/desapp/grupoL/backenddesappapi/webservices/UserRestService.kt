package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.JwtUserService
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.UserService
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.OkResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@EnableAutoConfiguration
class UserRestService {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var jwtUserService: JwtUserService

    @PostMapping("/api/user/register")
    @CrossOrigin
    fun register(@RequestBody user: User): ResponseEntity<*> {
        val userSaved = userService.save(user)
        val token = jwtUserService.createAuthenticationToken(userSaved)
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Authorization", token)
            .body(OkResponse())
    }
}