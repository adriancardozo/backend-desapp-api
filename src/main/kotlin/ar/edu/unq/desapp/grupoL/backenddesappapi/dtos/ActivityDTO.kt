package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.ActivityType

data class ActivityDTO(val hour: String,
                       val cryptocurrency: ActivityCryptoCurrencyDTO,
                       val amount: Double,
                       val amountARS: Double,
                       val user: SimpleUserDTO,
                       val type: ActivityType
                       )