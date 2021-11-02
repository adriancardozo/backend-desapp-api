package ar.edu.unq.desapp.grupoL.backenddesappapi

import ar.edu.unq.desapp.grupoL.backenddesappapi.services.CryptoCurrencyService
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.DollarDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.DollarQuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.QuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.MissingExternalDependencyException
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.CryptoCurrencyRepository
import org.junit.jupiter.api.Assertions.assertEquals
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
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ExtendWith(MockitoExtension::class)
//@TestPropertySource(properties = ["app.scheduling.enable=false"])
class CryptoCurrencyServiceTests {
    @MockBean
    private lateinit var restTemplate: RestTemplate
    @MockBean
    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository
    @Autowired
    @InjectMocks
    private lateinit var cryptoCurrencyService: CryptoCurrencyService
    @Value("\${app.cryptos}")
    private lateinit var cryptoCurrencyNamesSearched: List<String>

    @Test
    fun quotationsRequestsAreDelegatedToExternalWebService() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(someCryptoCurrencyDTOs()), aResponseEntity(someDollarDTOs()))
        val quotations = cryptoCurrencyService.quotations()
        assertEquals(cryptoCurrencyDTOSSearched(), toCryptoCurrencyDTOs(quotations))
    }

    @Test
    fun storeQuotationsRequestAreDelegatedToRepository() {
        `when`(cryptoCurrencyRepository.findAll()).thenReturn(toCryptoCurrencies(cryptoCurrencyDTOSSearched()))
        `when`(cryptoCurrencyRepository.isEmpty()).thenReturn(false)
        val storedQuotationsResult = cryptoCurrencyService.storedQuotations()
        assertEquals(cryptoCurrencyDTOSSearched(), toCryptoCurrencyDTOs(storedQuotationsResult))
    }

    @Test
    fun ifQuotationsNotStoredStoreQuotationsRequestsAreDelegatedToExternalWebService() {
        `when`(cryptoCurrencyRepository.findAll()).thenReturn(toCryptoCurrencies(cryptoCurrencyDTOSSearched()))
        `when`(cryptoCurrencyRepository.isEmpty()).thenReturn(true)
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(cryptoCurrencyDTOSSearched()), aResponseEntity(someDollarDTOs()))
        val storedQuotationsResult = cryptoCurrencyService.storedQuotations()
        assertEquals(cryptoCurrencyDTOSSearched(), toCryptoCurrencyDTOs(storedQuotationsResult))
    }

    @Test
    fun ifCannotGetCryptoCurrenciesQuotationGetQuotationsFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenThrow(RestClientException::class.java).thenReturn(aResponseEntity(someDollarDTOs()))
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.quotations() }
    }


    @Test
    fun ifCannotGetCryptoCurrenciesQuotationGetStoredQuotationsFail() {
        `when`(cryptoCurrencyRepository.isEmpty()).thenReturn(true)
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenThrow(RestClientException::class.java).thenReturn(aResponseEntity(someDollarDTOs()))
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.storedQuotations() }
    }

    @Test
    fun ifCannotGetDollarQuotationGetQuotationsFail() {
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(cryptoCurrencyDTOSSearched())).thenThrow(RestClientException::class.java)
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.quotations() }
    }

    @Test
    fun ifCannotGetDollarQuotationGetStoredQuotationsFail() {
        `when`(cryptoCurrencyRepository.isEmpty()).thenReturn(true)
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(cryptoCurrencyDTOSSearched())).thenThrow(RestClientException::class.java)
        assertThrows<MissingExternalDependencyException> { cryptoCurrencyService.storedQuotations() }
    }

    private fun <T> aResponseEntity(body: T): ResponseEntity<T> {
        return ResponseEntity(body, HttpStatus.OK)
    }

    private fun toCryptoCurrencyDTOs(quotations: List<QuotationDTO>): List<CryptoCurrencyDTO> {
        return quotations.map { CryptoCurrencyDTO(it.name, it.arPrice / dollarQuotation()) }
    }

    private fun toCryptoCurrencies(cryptoCurrencyDTOs: List<CryptoCurrencyDTO>): List<CryptoCurrency> {
        return cryptoCurrencyDTOs.map { CryptoCurrency(it.symbol, it.price * dollarQuotation(), 0)  }
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