package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.LoginUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.RegisterUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.SimpleUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.UserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.InvalidDataException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserAlreadyExistsException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
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

    @PostMapping("/api/user/register")
    @CrossOrigin
    fun register(@RequestBody user: RegisterUserDTO): ResponseEntity<*> {
        return try {
            val token = userService.register(user)
            ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", token)
                .body(OkResponse())
        } catch (e: InvalidDataException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Invalid data"))
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse("The user already exists"))
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }

    @PostMapping("/api/user/login")
    @CrossOrigin
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
    @CrossOrigin
    fun allUsers(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<SimpleUserDTO>>(userService.allUsers())
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/user/")
    @CrossOrigin
    fun user(@RequestHeader("authorization") token: String): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<UserDTO>(userService.user(token))
        } catch (e: Throwable) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("Bad Request"))
        }
    }
}