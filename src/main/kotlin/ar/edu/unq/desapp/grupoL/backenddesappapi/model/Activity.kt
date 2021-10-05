package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import java.time.LocalDateTime

data class Activity(var hour: LocalDateTime,
                    var cryptoCurrency: CryptoCurrency,
                    var user: User,
                    var amount: Double)