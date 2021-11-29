package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityQuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CreateActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.SimpleUserDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Activity
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.Quotation
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.ActivityRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.QuotationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ActivityService {
    @Autowired
    lateinit var repository: ActivityRepository
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtUserService: JwtUserService
    @Autowired
    lateinit var quotationRepository: QuotationRepository

    @Transactional
    fun createActivity(token: String, createActivity: CreateActivityDTO) {
        val user = userService.findByEmail(jwtUserService.userEmail(token))
        repository.save(
            Activity(
                LocalDateTime.now(),
                quotationRepository.save(Quotation(createActivity.cryptoName, createActivity.cryptoQuotation)),
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
                ActivityQuotationDTO(it.quotation.name, it.quotation.arPrice),
                it.amount,
                it.amount * it.quotation.arPrice,
                SimpleUserDTO(it.user.name, it.user.lastname, it.user.numberOfOperations, it.user.reputation(), it.user.email),
                it.type
            ) }
    }
}