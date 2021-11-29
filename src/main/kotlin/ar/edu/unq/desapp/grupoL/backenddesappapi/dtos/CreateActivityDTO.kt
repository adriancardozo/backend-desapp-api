package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.ActivityType
import java.time.LocalDateTime

data class CreateActivityDTO(var cryptoName: String, var cryptoQuotation: Double, var quotationHour: LocalDateTime, var amount: Double, var type: ActivityType)
