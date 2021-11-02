package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.CryptoCurrencyRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.DollarDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.QuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.MissingExternalDependencyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class CryptoCurrencyService {
    @Value("\${app.cryptos}")
    private lateinit var cryptoCurrenciesSearched: List<String>
    @Value("\${app.endpoint.crypto}")
    private lateinit var cryptoCurrencyApiEndPoint: String
    @Value("\${app.endpoint.dollar}")
    private lateinit var dollarApiEndPoint: String
    @Autowired
    private lateinit var restTemplate: RestTemplate
    @Autowired
    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    @Transactional
    fun storedQuotations(): List<QuotationDTO> {
        if(cryptoCurrencyRepository.isEmpty()) storeQuotations()
        return toQuotationDTOs(cryptoCurrencyRepository.findAll())
    }

    fun quotations(): List<QuotationDTO> = toQuotationDTOs(cryptoCurrencies())

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

    private fun toQuotationDTOs(cryptoCurrencies: List<CryptoCurrency>): List<QuotationDTO> {
        return cryptoCurrencies.map {
            QuotationDTO(
                it.name,
                it.arPrice,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it.quotationHour), ZoneId.systemDefault())
            )
        }
    }

    private fun cryptoCurrencies(): List<CryptoCurrency> = allCryptoCurrencies().filter { cryptoCurrenciesSearched.contains(it.name) }

    private fun allCryptoCurrencies(): List<CryptoCurrency> {
        val response = getListRequest(cryptoCurrencyApiEndPoint, object : ParameterizedTypeReference<List<CryptoCurrencyDTO>>(){})
        val quotationHour: Long = System.currentTimeMillis()
        val dollarQuotation: Double = arsDollarQuotation()
        return (response.body ?: emptyList()).map { CryptoCurrency(it.symbol, it.price * dollarQuotation, quotationHour) }
    }

    private fun arsDollarQuotation(): Double {
        val response = getListRequest(dollarApiEndPoint, object : ParameterizedTypeReference<List<DollarDTO>>(){})
        return response.body?.first { it.casa.nombre == "Dolar Oficial" }?.casa?.compra?.replace(",", ".")?.toDouble() ?: throw MissingExternalDependencyException()
    }

    @Scheduled(fixedRate = 600000)
    protected fun storeQuotations() = cryptoCurrencyRepository.saveAll(cryptoCurrencies())

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