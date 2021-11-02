package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Activity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActivityRepository: JpaRepository<Activity, Int> {
    override fun findById(id: Int): Optional<Activity>

    override fun findAll(): List<Activity>
}