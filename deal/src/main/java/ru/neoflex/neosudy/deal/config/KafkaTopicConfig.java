package ru.neoflex.neosudy.deal.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import ru.neoflex.neosudy.deal.model.types.Theme;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder.name(Theme.FINISH_REGISTRATION.getTopicName())
                           .build();
    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return TopicBuilder.name(Theme.CREATE_DOCUMENTS.getTopicName())
                           .build();
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return TopicBuilder.name(Theme.SEND_DOCUMENTS.getTopicName())
                           .build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder.name(Theme.SEND_SES.getTopicName())
                           .build();
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return TopicBuilder.name(Theme.CREDIT_ISSUED.getTopicName())
                           .build();
    }

    @Bean
    public NewTopic applicationDeniedTopic() {
        return TopicBuilder.name(Theme.APPLICATION_DENIED.getTopicName())
                           .build();
    }
}