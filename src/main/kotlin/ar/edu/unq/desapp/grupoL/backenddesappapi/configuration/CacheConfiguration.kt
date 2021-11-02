package ar.edu.unq.desapp.grupoL.backenddesappapi.configuration

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.QuotationDTO
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CacheConfiguration {
    @Bean
    fun cacheManager(): CacheManager = ConcurrentMapCacheManager("quotations")
}