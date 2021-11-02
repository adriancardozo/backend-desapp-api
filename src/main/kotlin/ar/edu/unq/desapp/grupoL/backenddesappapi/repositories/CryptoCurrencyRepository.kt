package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CryptoCurrencyRepository : JpaRepository<CryptoCurrency, Int> {
    override fun findById(id: Int): Optional<CryptoCurrency>

    override fun findAll(): List<CryptoCurrency>
}