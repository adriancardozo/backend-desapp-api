package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {
    @Autowired
    lateinit var repository: UserRepository

    @Transactional
    fun save(model: User): User {
        return repository.save(model)
    }

    fun findByID(id: Int): User {
        return repository.findById(id).get()
    }
}