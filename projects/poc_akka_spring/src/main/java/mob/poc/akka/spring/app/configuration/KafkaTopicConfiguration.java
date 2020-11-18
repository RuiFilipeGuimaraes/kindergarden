package mob.poc.akka.spring.app.configuration;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${topic.name.consumer}")
    private String consumerTopicName;

    @Bean
    public NewTopic dataTopic() {
        return TopicBuilder.name(consumerTopicName)
                .partitions(6)
                .replicas(3)
                .build();
    }
}
