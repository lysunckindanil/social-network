package org.example.postsservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic share_friends() {
        return TopicBuilder.name("share-subscribers").build();
    }

    @Bean
    public NewTopic delete_from_friends() {
        return TopicBuilder.name("delete-from-subscribers").build();
    }
}
