package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.CryptoCurrencyService
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.exceptions.MissingExternalDependencyException
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.ErrorResponse
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.OkResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
class CryptoCurrencyRestService {
    @Autowired
    private lateinit var cryptoCurrencyService: CryptoCurrencyService

    @GetMapping("/api/crypto/quotation")
    fun quotations(): ResponseEntity<*> {
        return try {
            val cryptoCurrencies: List<CryptoCurrency> = cryptoCurrencyService.cryptoCurrencies()
            ResponseEntity.ok().body<List<CryptoCurrency>>(cryptoCurrencies)
        } catch (e: MissingExternalDependencyException) {
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(OkResponse("Service Unavailable"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(OkResponse("Bad Request"))
        }
    }

    @GetMapping("/api/crypto")
    fun cryptoCurrencies(): ResponseEntity<*> {
        return try {
            val cryptoCurrencies: List<CryptoCurrency> = cryptoCurrencyService.allCryptoCurrencies()
            return ResponseEntity.ok().body<List<CryptoCurrency>>(cryptoCurrencies)
        } catch (e: MissingExternalDependencyException) {
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse("Service Unavailable"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }
}