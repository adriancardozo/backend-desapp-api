package ar.edu.unq.desapp.grupoL.backenddesappapi.services

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

    @Transactional
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