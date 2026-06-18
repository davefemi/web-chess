package nl.davefemi.chess.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.data.entity.session.CredentialEntity;
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
    public RedisTemplate<String, CredentialEntity> credentialRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, CredentialEntity> template = new RedisTemplate<>();


        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, CredentialEntity.class));

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, CredentialEntity.class));

        template.afterPropertiesSet();

        return template;
    }
}