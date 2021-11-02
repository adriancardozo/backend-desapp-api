package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.LoginUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.UserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserAlreadyExistsException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.JwtUserService
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.UserService
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.ErrorResponse
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.OkResponse
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@EnableAutoConfiguration
@Api(value = "user", description = "Rest API for user operations", tags = ["User API"])
class UserRestService {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var jwtUserService: JwtUserService

    @PostMapping("/api/user/register")
    @CrossOrigin
    fun register(@RequestBody user: User): ResponseEntity<*> {
        return try {
            val token = userService.register(user)
            ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", token)
                .body(OkResponse())
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse("The user already exists"))
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }

    @PostMapping("/api/user/login")
    fun login(@RequestBody loginUser: LoginUserDTO): ResponseEntity<*>? {
        return try {
            val token = userService.login(loginUser)
            ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", token)
                .body(OkResponse())
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The user not exists"))
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/users/")
    fun allUsers(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<UserDTO>>(userService.allUsers())
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/user/")
    fun user(@RequestHeader("authorization") token: String): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<UserDTO>(userService.user(token))
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }
}