package ar.edu.unq.desapp.grupoL.backenddesappapi.repositories

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency

interface RedisRepository {
    fun isEmpty(): Boolean
    fun findAll(): List<CryptoCurrency>
    fun saveAll(cryptoCurrencies: List<CryptoCurrency>)
    fun deleteAll()
}