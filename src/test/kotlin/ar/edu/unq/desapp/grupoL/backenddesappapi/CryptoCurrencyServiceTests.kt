package ar.edu.unq.desapp.grupoL.backenddesappapi

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.CryptoCurrencyService
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.CryptoCurrencyDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.dtos.DollarDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ExtendWith(MockitoExtension::class)
class CryptoCurrencyServiceTests {
    @Mock
    private lateinit var restTemplate: RestTemplate
    @Autowired
    @InjectMocks
    private lateinit var cryptoCurrencyService: CryptoCurrencyService

    @BeforeEach
    fun beforeTest(){
        `when`(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference::class.java)))
            .thenReturn(aResponseEntity(someCryptoCurrencyDTOs()), aResponseEntity(someDollarDTOs()))
    }

    @Test
    fun allCryptoCurrenciesRequestAreDelegatedToExternalWebService() {
        val cryptoCurrenciesResult = cryptoCurrencyService.allCryptoCurrencies()
        assertEquals(someCryptoCurrencyDTOs(), toCryptoCurrencyDTOs(cryptoCurrenciesResult))
    }

    @Test
    fun cryptoCurrenciesRequestAreDelegatedToExternalWebService() {
        val cryptoCurrenciesResult = cryptoCurrencyService.cryptoCurrencies()
        assertEquals(cryptoCurrencyDTOSSearched(), toCryptoCurrencyDTOs(cryptoCurrenciesResult))
    }

    private fun <T> aResponseEntity(body: T): ResponseEntity<T> {
        return ResponseEntity(body, HttpStatus.OK)
    }

    private fun toCryptoCurrencyDTOs(cryptoCurrencies: List<CryptoCurrency>): List<CryptoCurrencyDTO> {
        return cryptoCurrencies.map { CryptoCurrencyDTO(it.name, it.arPrice / dollarQuotation()) }
    }

    private fun dollarQuotation(): Double {
        return someDollarDTOs().last().v
    }

    private fun someCryptoCurrencyDTOs(): List<CryptoCurrencyDTO> {
        return listOf(CryptoCurrencyDTO("CRYPTO",500.0)) + cryptoCurrencyDTOSSearched()
    }

    private fun someDollarDTOs(): List<DollarDTO> {
        return listOf(DollarDTO("",100.0))
    }

    private fun cryptoCurrencyDTOSSearched(): List<CryptoCurrencyDTO> {
        return cryptoCurrencyNamesSearched().map { CryptoCurrencyDTO(it, 200.0) }
    }

    private fun cryptoCurrencyNamesSearched(): Set<String> {
        return setOf(
            "ALICEUSDT", "MATICUSDT", "AXSUSDT", "AAVEUSDT",
            "ATOMUSDT", "NEOUSDT", "DOTUSDT", "ETHUSDT",
            "CAKEUSDT", "BTCUSDT", "BNBUSDT", "ADAUSDT",
            "TRXUSDT", "AUDIOUSDT"
        )
    }
}