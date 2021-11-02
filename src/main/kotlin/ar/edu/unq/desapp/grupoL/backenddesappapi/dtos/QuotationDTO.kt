package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import java.time.LocalDateTime

data class QuotationDTO(var name: String,
                        var arPrice: Double,
                        var quotationHour: LocalDateTime)
