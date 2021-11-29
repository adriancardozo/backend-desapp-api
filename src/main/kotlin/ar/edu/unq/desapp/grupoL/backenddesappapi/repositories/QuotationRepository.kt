package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Quotation
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QuotationRepository : JpaRepository<Quotation, Int> {
    override fun findById(id: Int): Optional<Quotation>

    override fun findAll(): List<Quotation>
}