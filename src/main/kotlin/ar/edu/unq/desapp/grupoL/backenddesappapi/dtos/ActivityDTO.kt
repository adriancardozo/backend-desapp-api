package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

data class ActivityDTO(val hour: String,
                       val cryptoCurrencyName: String,
                       val cryptoCurrencyAmount: Double,
                       val cryptoCurrencyQuotation: Double,
                       val arsOperationAmount: Double,
                       val user: SimpleUserDTO
                       )