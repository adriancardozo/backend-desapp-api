package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.DollarDTO
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class CryptoCurrencyService {

    val cryptoCurrenciesSearched: Set<String> = setOf(
        "ALICEUSDT",
        "MATICUSDT",
        "AXSUSDT",
        "AAVEUSDT",
        "ATOMUSDT",
        "NEOUSDT",
        "DOTUSDT",
        "ETHUSDT",
        "CAKEUSDT",
        "BTCUSDT",
        "BNBUSDT",
        "ADAUSDT",
        "TRXUSDT",
        "AUDIOUSDT"
    )

    private val cryptoCurrencyApiEndPoint = "https://api1.binance.com/api/v3/ticker/price"
    private val dollarApiEndPoint = "https://api.estadisticasbcra.com/usd_of"

    fun cryptoCurrencies() : List<CryptoCurrency> {
        return allCryptoCurrencies().filter { cryptoCurrenciesSearched.contains(it.name) }
    }

    fun allCryptoCurrencies(): List<CryptoCurrency> {
        val response = RestTemplate().exchange(
            cryptoCurrencyApiEndPoint,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<CryptoCurrencyDTO>>() {}
        )
        val quotationHour: LocalDateTime = LocalDateTime.now()
        val dollarQuotation: Double = arsDollarQuotation()
        return (response.body ?: emptyList())
            .map { CryptoCurrency(it.symbol, it.price * dollarQuotation, quotationHour) }
    }

    private fun arsDollarQuotation(): Double {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", "BEARER eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjU0MzcxMjAsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJjYXJkb3pvYWRyaWFuOTk3QGdtYWlsLmNvbSJ9.eKm7XlvnqTNlx_0WNYPsENWSu1mDidgtCxtdpzEGdZlqarZdeRQauavp0QD_hTETgxcROUpljHgtmY2ZBMf2Ag")
        val requestEntity = HttpEntity("parameters", headers)
        val response = RestTemplate().exchange(
            dollarApiEndPoint,
            HttpMethod.GET,
            requestEntity,
            object : ParameterizedTypeReference<List<DollarDTO>>() {}
        )
        return response.body?.last()?.v!!
    }
}