package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: JpaRepository<User, Int> {
    override fun findById(id: Int): Optional<User>

    override fun findAll(): List<User>

    fun findByEmail(email: String): User
}