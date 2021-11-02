package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.QuotationDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.MissingExternalDependencyException
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.SpringCryptoCurrencyService
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.ErrorResponse
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
@Api(value = "crypto-currency", description = "Rest API for cryptocurrency operations", tags = ["Cryptocurrency API"])
class CryptoCurrencyRestService {
    @Autowired
    private lateinit var cryptoCurrencyService: SpringCryptoCurrencyService

    @GetMapping("/api/crypto/quotation")
    @CrossOrigin
    //@ApiResponses(value = [ApiResponse(code = 200, message = "ok", response = [CryptoCurrency]::class)])
    fun quotations(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<QuotationDTO>>(cryptoCurrencyService.storedQuotations())
        } catch (e: MissingExternalDependencyException) {
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse("Service Unavailable"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/crypto")
    @CrossOrigin
    fun cryptoCurrencies(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<QuotationDTO>>(cryptoCurrencyService.quotations())
        } catch (e: MissingExternalDependencyException) {
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse("Service Unavailable"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }


}