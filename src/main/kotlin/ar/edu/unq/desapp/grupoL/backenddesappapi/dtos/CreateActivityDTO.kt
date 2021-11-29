package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.ActivityType

data class CreateActivityDTO(var cryptoName: String, var cryptoQuotation: Double, var amount: Double, var type: ActivityType)
