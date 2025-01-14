package org.example.friendspostsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.MessageConverter;

@Configuration
public class KafkaConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new JsonMessageConverter();
    }
}
