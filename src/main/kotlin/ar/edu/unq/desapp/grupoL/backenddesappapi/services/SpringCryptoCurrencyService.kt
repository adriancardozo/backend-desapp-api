package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.QuotationDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class SpringCryptoCurrencyService: CryptoCurrencyService() {
    @Autowired
    private lateinit var cacheManager: CacheManager

    override fun storedQuotations(): List<QuotationDTO> {
        return cacheManager.getCache("quotations")?.get("quotationList")?.get() as List<QuotationDTO>
    }

    @Scheduled(fixedRate = 600000)
    fun storeQuotations() = cacheManager.getCache("quotations")?.put("quotationList", quotations())
}