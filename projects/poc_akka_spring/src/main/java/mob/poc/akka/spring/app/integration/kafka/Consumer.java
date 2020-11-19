package mob.poc.akka.spring.app.integration.kafka;

import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private NewTopic newTopic;

    @Autowired
    private SampleDataRepository repository;

    @KafkaListener(topics = "${topic.name.consumer}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord) throws IOException {
        logger.info(String.format("#### -> Consumed message with key=%s, partition=%s, offset=%s. Message -> %s",
                consumerRecord.key(), consumerRecord.partition(), consumerRecord.offset(), consumerRecord.value()));

        repository.save(SampleData.newBuilder()
                .withInfo(consumerRecord.value())
                .withKey(consumerRecord.key())
                .withOffset(String.valueOf(consumerRecord.offset()))
                .withReceivedTimestamp(String.valueOf(consumerRecord.timestamp()))
                .withPartition(String.valueOf(consumerRecord.partition()))
                .build());
    }
}
