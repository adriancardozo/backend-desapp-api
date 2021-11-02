package ar.edu.unq.desapp.grupoL.backenddesappapi.configuration

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.CryptoCurrency
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.DeserializerFactory
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer

import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
class RedisConfiguration {
    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory()
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, CryptoCurrency> {
        val redisTemplate: RedisTemplate<String, CryptoCurrency> = RedisTemplate()
        redisTemplate.setConnectionFactory(jedisConnectionFactory())
        val serializer = Jackson2JsonRedisSerializer(CryptoCurrency::class.java)
        serializer.setObjectMapper(ObjectMapper().configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false).registerModule(JavaTimeModule()).registerModule(Jdk8Module()))
        redisTemplate.setDefaultSerializer(serializer)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = GenericJackson2JsonRedisSerializer()
        redisTemplate.valueSerializer = serializer//Jackson2JsonRedisSerializer(CryptoCurrency::class.java)
        return redisTemplate
    }
}