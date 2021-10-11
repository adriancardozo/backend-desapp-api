package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.CryptoCurrencyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
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
        val cryptoCurrencies: List<CryptoCurrency> = cryptoCurrencyService.cryptoCurrencies()
        return ResponseEntity.ok().body<List<CryptoCurrency>>(cryptoCurrencies)
    }

    @GetMapping("/api/crypto")
    fun cryptoCurrencies(): ResponseEntity<*> {
        val cryptoCurrencies: List<CryptoCurrency> = cryptoCurrencyService.allCryptoCurrencies()
        return ResponseEntity.ok().body<List<CryptoCurrency>>(cryptoCurrencies)
    }
}