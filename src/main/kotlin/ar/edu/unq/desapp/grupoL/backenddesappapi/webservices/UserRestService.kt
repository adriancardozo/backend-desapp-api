package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
@EnableAutoConfiguration
class UserRestService {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/api/user/register")
    fun register(@RequestBody user: User): ResponseEntity<*> {
        val userSaved = userService.save(user)
        return ResponseEntity.created(URI.create("/api/user/${userSaved.id}")).body<User>(userSaved)
    }
}