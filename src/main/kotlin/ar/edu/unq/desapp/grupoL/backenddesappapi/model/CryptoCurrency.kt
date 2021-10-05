package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import java.time.LocalDateTime

data class CryptoCurrency(var name: String,
                          var arPrice: Double,
                          var quotationHour: LocalDateTime)