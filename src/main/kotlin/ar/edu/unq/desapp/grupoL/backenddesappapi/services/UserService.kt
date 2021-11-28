package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.LoginUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.RegisterUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.SimpleUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.UserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.InvalidDataException
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.UserRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserAlreadyExistsException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {
    @Autowired
    lateinit var repository: UserRepository
    @Autowired
    private lateinit var jwtUserService: JwtUserService

    fun login(loginUser: LoginUserDTO): String {
        if(!(isValidEmail(loginUser.email) && isValidPassword(loginUser.password))) throw UserNotFoundException()
        val user = findByEmail(loginUser.email)
        if(user.password != loginUser.password) throw UserNotFoundException()
        return jwtUserService.createAuthenticationToken(user)
    }

    @Transactional
    fun register(registerUser: RegisterUserDTO): String {
        checkIsValidUser(registerUser)
        val userSaved = save(toUser(registerUser))
        return jwtUserService.createAuthenticationToken(userSaved)
    }

    private fun toUser(registerUser: RegisterUserDTO): User {
        return User(
            registerUser.name,
            registerUser.lastname,
            registerUser.email,
            registerUser.address,
            registerUser.password,
            registerUser.cvu,
            registerUser.walletAddress
        )
    }

    private fun checkIsValidUser(user: RegisterUserDTO) = if(!isValidUser(user)) throw InvalidDataException() else Unit

    private fun isValidUser(user: RegisterUserDTO) = isValidName(user.name) &&
            isValidName(user.lastname) &&
            isValidEmail(user.email) &&
            isValidAddress(user.address) &&
            isValidPassword(user.password) &&
            isValidCvu(user.cvu) &&
            isValidWalletAddress(user.walletAddress)

    private fun isValidPassword(password: String): Boolean = password.length >= 8 && password.matches(Regex("^(?=.*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9])(?=.*[a-z]).{8,}\$"))

    private fun isValidWalletAddress(walletAddress: String): Boolean = walletAddress.length == 8 && walletAddress.matches(Regex("^\\d+\$"))

    private fun isValidCvu(cvu: String): Boolean = cvu.length == 22 && cvu.matches(Regex("^\\d+\$"))

    private fun isValidAddress(address: String): Boolean = address.length in 10..30

    private fun isValidName(name: String): Boolean = name.length in 3..30

    private fun isValidEmail(email: String): Boolean = email.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))

    fun user(token: String): UserDTO = toUserDTO(findByEmail(jwtUserService.userEmail(token)))

    fun allUsers(): List<SimpleUserDTO> = repository.findAll().map {
        SimpleUserDTO(it.name, it.lastname, it.numberOfOperations, it.reputation(), it.email)
    }

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

    private fun checkUserNotExists(email: String) = if(getUserByEmailOrNull(email) != null) throw UserAlreadyExistsException() else Unit

    private fun getUserByEmailOrNull(email: String): User? {
        return try {
            repository.findByEmail(email)
        } catch (e: EmptyResultDataAccessException){
            null
        }
    }
}