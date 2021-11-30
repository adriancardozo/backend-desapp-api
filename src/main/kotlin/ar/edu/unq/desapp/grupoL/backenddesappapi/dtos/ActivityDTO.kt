package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.ActivityType

data class ActivityDTO(val id: Int,
                       val hour: String,
                       val cryptocurrency: ActivityQuotationDTO,
                       val amount: Double,
                       val amountARS: Double,
                       val user: ActivityUserDTO,
                       val type: ActivityType
                       )