package nl.davefemi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.davefemi.data.entity.AccessCodeEntity;
import nl.davefemi.data.entity.GameSessionEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    public final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, GameSessionEntity> gameSessionRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, GameSessionEntity> template = new RedisTemplate<>();


        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, GameSessionEntity.class));

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, GameSessionEntity.class));

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisTemplate<String, AccessCodeEntity> accessCodeRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, AccessCodeEntity> template = new RedisTemplate<>();


        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, AccessCodeEntity.class));

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, AccessCodeEntity.class));

        template.afterPropertiesSet();

        return template;
    }
}