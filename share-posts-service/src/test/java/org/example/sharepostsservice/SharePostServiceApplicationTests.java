package org.example.sharepostsservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SharePostServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public KafkaTemplate kafkaTemplate() {

            return Mockito.mock(KafkaTemplate.class);
        }
    }
}
