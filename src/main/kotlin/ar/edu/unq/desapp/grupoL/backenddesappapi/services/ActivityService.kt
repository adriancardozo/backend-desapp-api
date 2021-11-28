package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.SimpleUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.ActivityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActivityService {
    @Autowired
    lateinit var repository: ActivityRepository

    fun allActivities(): List<ActivityDTO> {
        return repository.findAll().map {
            ActivityDTO(
                it.hour.toString(),
                it.cryptoCurrency.name,
                it.amount,
                it.cryptoCurrency.arPrice,
                it.amount * it.cryptoCurrency.arPrice,
                SimpleUserDTO(it.user.name, it.user.lastname, it.user.numberOfOperations, it.user.reputation(), it.user.email)) }
    }
}