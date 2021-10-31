package ar.edu.unq.desapp.grupoL.backenddesappapi

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.CryptoCurrencyService
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.DollarDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.DollarQuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.exceptions.MissingExternalDependencyException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ExtendWith(MockitoExtension::class)
class CryptoCurrencyServiceTests {
    @Mock
    private lateinit var restTemplate: RestTemplate
    @Autowired
    @InjectMocks
    private lateinit var cryptoCurrencyService: CryptoCurrencyService
    @Value("\${app.cryptos}")
    private lateinit var cryptoCurrencyNamesSearched: List<String>

    @Test
    fun allCryptoCurrenciesRequestAreDelegatedToExternalWebService() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(someCryptoCurrencyDTOs()), aResponseEntity(someDollarDTOs()))
        val cryptoCurrenciesResult = cryptoCurrencyService.allCryptoCurrencies()
        assertEquals(someCryptoCurrencyDTOs(), toCryptoCurrencyDTOs(cryptoCurrenciesResult))
    }

    @Test
    fun cryptoCurrenciesRequestAreDelegatedToExternalWebService() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(someCryptoCurrencyDTOs()), aResponseEntity(someDollarDTOs()))
        val cryptoCurrenciesResult = cryptoCurrencyService.cryptoCurrencies()
        assertEquals(cryptoCurrencyDTOSSearched(), toCryptoCurrencyDTOs(cryptoCurrenciesResult))
    }

    @Test
    fun ifCannotGetCryptoCurrenciesQuotationGetAllCryptoCurrenciesFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenThrow(RestClientException::class.java).thenReturn(aResponseEntity(someDollarDTOs()))
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.allCryptoCurrencies() }
    }


    @Test
    fun ifCannotGetCryptoCurrenciesQuotationGetCryptoCurrenciesFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenThrow(RestClientException::class.java).thenReturn(aResponseEntity(someDollarDTOs()))
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.cryptoCurrencies() }
    }

    @Test
    fun ifCannotGetDollarQuotationGetAllCryptoCurrenciesFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(cryptoCurrencyDTOSSearched())).thenThrow(RestClientException::class.java)
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.allCryptoCurrencies() }
    }

    @Test
    fun ifCannotGetDollarQuotationGetCryptoCurrenciesFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(cryptoCurrencyDTOSSearched())).thenThrow(RestClientException::class.java)
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.cryptoCurrencies() }
    }


    private fun <T> aResponseEntity(body: T): ResponseEntity<T> {
        return ResponseEntity(body, HttpStatus.OK)
    }

    private fun toCryptoCurrencyDTOs(cryptoCurrencies: List<CryptoCurrency>): List<CryptoCurrencyDTO> {
        return cryptoCurrencies.map { CryptoCurrencyDTO(it.name, it.arPrice / dollarQuotation()) }
    }

    private fun dollarQuotation(): Double {
        return someDollarDTOs().first { it.casa.nombre == "Dolar Oficial" }.casa.compra?.replace(",", ".")?.toDouble()!!
    }

    private fun someCryptoCurrencyDTOs(): List<CryptoCurrencyDTO> {
        return listOf(CryptoCurrencyDTO("CRYPTO",500.0)) + cryptoCurrencyDTOSSearched()
    }

    private fun someDollarDTOs(): List<DollarDTO> {
        return listOf(DollarDTO(DollarQuotationDTO("100,0", "100,0", "Dolar Oficial")))
    }

    private fun cryptoCurrencyDTOSSearched(): List<CryptoCurrencyDTO> {
        return cryptoCurrencyNamesSearched.map { CryptoCurrencyDTO(it, 200.0) }
    }
}