package com.cakk.api.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Duration

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator

@Configuration
@EnableCaching
class CacheConfig {

	@Bean
	fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
		val polymorphicTypeValidator = BasicPolymorphicTypeValidator.builder()
			.allowIfBaseType(Object::class.java)
			.build()

		val objectMapper = ObjectMapper()
		objectMapper.activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)

		val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					GenericJackson2JsonRedisSerializer(objectMapper)
				)
			)
			.entryTtl(Duration.ofMinutes(15L))

		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory)
			.cacheDefaults(redisCacheConfiguration)
			.build()
	}
}
