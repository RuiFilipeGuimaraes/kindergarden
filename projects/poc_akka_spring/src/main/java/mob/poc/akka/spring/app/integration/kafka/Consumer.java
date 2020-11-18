package mob.poc.akka.spring.app.integration.kafka;

import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
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
    public void consume(@Payload String message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.MESSAGE_KEY) String messageKey,
                        @Header(KafkaHeaders.OFFSET) String offset,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String receivedTimestamp) throws IOException {
        logger.info(String.format("#### -> Consumed message with key=%s, partition=%s, offset=%s. Message -> %s", messageKey, partition, offset, message));

        repository.add(SampleData.newBuilder()
                .withInfo(message)
                .withKey(messageKey)
                .withOffset(offset)
                .withReceivedTimestamp(receivedTimestamp)
                .build());
    }
}
