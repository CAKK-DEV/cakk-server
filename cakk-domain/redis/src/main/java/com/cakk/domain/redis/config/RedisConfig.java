package com.cakk.domain.redis.config;

import static java.util.Objects.*;

import java.time.Duration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

	private final int port;
	private final String host;
	private final String address;
	private final String password;

	public RedisConfig(
		@Value("${spring.data.redis.port}")
		int port,
		@Value("${spring.data.redis.host}")
		String host,
		@Value("${spring.data.redis.password}")
		String password
	) {
		this.port = port;
		this.host = host;
		this.address = "redis://%s:%d".formatted(host, port);
		this.password = password;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);

		final String profile = System.getProperty("spring.profiles.active");
		if (nonNull(profile) && profile.equals("prod")) {
			redisStandaloneConfiguration.setPassword(password);
		}

		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public RedissonClient redissonClient() {
		final Config config = new Config();

		SingleServerConfig ssc = config.useSingleServer();
		ssc.setAddress(address);

		final String profile = System.getProperty("spring.profiles.active");
		if (nonNull(profile) && profile.equals("prod")) {
			ssc.setPassword(password);
		}

		return Redisson.create(config);
	}

	@Bean
	public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
			.entryTtl(Duration.ofMinutes(15L));

		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory)
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}
}
