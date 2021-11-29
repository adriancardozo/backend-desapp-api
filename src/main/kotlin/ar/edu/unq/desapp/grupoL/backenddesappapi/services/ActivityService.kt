package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityCryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CreateActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.SimpleUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Activity
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.ActivityRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.CryptoCurrencyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ActivityService {
    @Autowired
    lateinit var repository: ActivityRepository
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtUserService: JwtUserService
    @Autowired
    lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    @Transactional
    fun createActivity(token: String, createActivity: CreateActivityDTO) {
        val user = userService.findByEmail(jwtUserService.userEmail(token))
        repository.save(
            Activity(
                LocalDateTime.now(),
                cryptoCurrencyRepository.save(CryptoCurrency(createActivity.cryptoName, createActivity.cryptoQuotation, createActivity.quotationHour.atZone(
                    ZoneId.systemDefault()).toInstant().toEpochMilli())),
                user,
                createActivity.amount,
                createActivity.type
            )
        )
    }

    fun allActivities(): List<ActivityDTO> {
        return repository.findAll().map {
            ActivityDTO(
                it.hour.toString(),
                ActivityCryptoCurrencyDTO(it.cryptoCurrency.name, it.cryptoCurrency.arPrice),
                it.amount,
                it.amount * it.cryptoCurrency.arPrice,
                SimpleUserDTO(it.user.name, it.user.lastname, it.user.numberOfOperations, it.user.reputation(), it.user.email),
                it.type
            ) }
    }
}