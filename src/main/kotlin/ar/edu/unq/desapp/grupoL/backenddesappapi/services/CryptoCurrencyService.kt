package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.DollarDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.exceptions.MissingExternalDependencyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class CryptoCurrencyService {
    @Value("\${app.cryptos}")
    private lateinit var cryptoCurrenciesSearched: List<String>
    @Value("\${app.endpoint.crypto}")
    private lateinit var cryptoCurrencyApiEndPoint: String
    @Value("\${app.endpoint.dollar}")
    private lateinit var dollarApiEndPoint: String
    @Value("\${app.token.bcra}")
    private lateinit var dollarToken: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun cryptoCurrencies() : List<CryptoCurrency> {
        return allCryptoCurrencies().filter { cryptoCurrenciesSearched.contains(it.name) }
    }

    fun allCryptoCurrencies(): List<CryptoCurrency> {
        val response = getListRequest(cryptoCurrencyApiEndPoint, object : ParameterizedTypeReference<List<CryptoCurrencyDTO>>(){})
        val quotationHour: LocalDateTime = LocalDateTime.now()
        val dollarQuotation: Double = arsDollarQuotation()
        return (response.body ?: emptyList())
            .map { CryptoCurrency(it.symbol, it.price * dollarQuotation, quotationHour) }
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    private fun arsDollarQuotation(): Double {
        val response = getListRequest(dollarApiEndPoint, object : ParameterizedTypeReference<List<DollarDTO>>(){}, requestEntityDollarQuotation())
        return response.body?.last()?.v ?: throw MissingExternalDependencyException()
    }

    private fun requestEntityDollarQuotation(): HttpEntity<String> {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", dollarToken)
        return HttpEntity("parameters", headers)
    }

    private fun<T> getListRequest(endPoint: String, parameterizedTypeReference: ParameterizedTypeReference<T>, requestEntity: HttpEntity<String>? = null): ResponseEntity<T> {
        try {
            return restTemplate.exchange(
                endPoint,
                HttpMethod.GET,
                requestEntity,
                parameterizedTypeReference
            )
        }
        catch (e: RestClientException){
            throw MissingExternalDependencyException()
        }
    }
}