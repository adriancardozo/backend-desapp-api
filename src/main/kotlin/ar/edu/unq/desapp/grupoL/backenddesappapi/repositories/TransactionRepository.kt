package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Activity
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {
    override fun findById(id: Long): Optional<Transaction>

    override fun findAll(): List<Transaction>
}