package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.*
import javax.annotation.PostConstruct

@Repository
class CryptoCurrencyRepository: RedisRepository {
    private val key: String = "CryptoCurrency"
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, CryptoCurrency>
    private lateinit var hashOperations: HashOperations<String, String, CryptoCurrency>

    @PostConstruct
    fun init() {
        hashOperations = redisTemplate.opsForHash()
    }

    override fun isEmpty(): Boolean {
        return findAll().isEmpty()
    }

    override fun findAll(): List<CryptoCurrency> {
        return hashOperations.entries(key).values.toList()
    }

    override fun saveAll(cryptoCurrencies: List<CryptoCurrency>) {
        hashOperations.putAll(key, cryptoCurrencies.associateBy { it.name })
    }

    override fun deleteAll() {
        redisTemplate.delete(key)
    }
}