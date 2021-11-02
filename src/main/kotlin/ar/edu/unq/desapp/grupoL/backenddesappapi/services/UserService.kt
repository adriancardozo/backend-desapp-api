package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.LoginUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.UserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.UserRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserAlreadyExistsException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.OkResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {
    @Autowired
    lateinit var repository: UserRepository
    @Autowired
    private lateinit var jwtUserService: JwtUserService

    fun login(loginUser: LoginUserDTO): String {
        val user = findByEmail(loginUser.email)
        if(user.password != loginUser.password) throw UserNotFoundException()
        return jwtUserService.createAuthenticationToken(user)
    }

    @Transactional
    fun register(user: User): String {
        val userSaved = save(user)
        return jwtUserService.createAuthenticationToken(userSaved)
    }

    fun user(token: String): UserDTO {
        return toUserDTO(findByEmail(jwtUserService.userEmail(token)))
    }

    fun allUsers(): List<UserDTO> = repository.findAll().map { toUserDTO(it) }

    private fun toUserDTO(user: User): UserDTO {
        return UserDTO(
            user.name,
            user.lastname,
            user.email,
            user.address,
            user.cvu,
            user.walletAddress,
            user.points,
            user.numberOfOperations,
            user.reputation()
        )
    }

    fun save(model: User): User {
        checkUserNotExists(model.email)
        return repository.save(model)
    }

    fun findByID(id: Int): User {
        val recoveredUser = repository.findById(id)
        if(recoveredUser.isEmpty) throw UserNotFoundException()
        return recoveredUser.get()
    }

    fun findByEmail(email: String): User {
        try {
            return repository.findByEmail(email)
        }
        catch (e: EmptyResultDataAccessException){
            throw UserNotFoundException()
        }
    }

    private fun checkUserNotExists(email: String) {
        if(getUserByEmailOrNull(email) != null) throw UserAlreadyExistsException()
    }

    private fun getUserByEmailOrNull(email: String): User? {
        return try {
            repository.findByEmail(email)
        } catch (e: EmptyResultDataAccessException){
            null
        }
    }
}