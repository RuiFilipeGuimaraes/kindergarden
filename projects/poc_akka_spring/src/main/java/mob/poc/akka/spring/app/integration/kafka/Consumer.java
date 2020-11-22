package mob.poc.akka.spring.app.integration.kafka;

import mob.poc.akka.spring.app.integration.akka.AkkaSystemImpl;
import mob.poc.akka.spring.app.model.Record;
import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private NewTopic newTopic;

    @Autowired
    private AkkaSystemImpl akkaSystem;

    @Autowired
    private SampleDataRepository repository;

    @KafkaListener(topics = "${topic.name.consumer}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> consumerRecord,
                        Acknowledgment acknowledgment) {
        logger.info(String.format("#### -> Consumed message with key=%s, partition=%s, offset=%s. Message -> %s",
                consumerRecord.key(), consumerRecord.partition(), consumerRecord.offset(), consumerRecord.value()));

        final String messageIdentificationKey = String.format("K_%s_%s_%s", consumerRecord.partition(),
                consumerRecord.offset(), System.currentTimeMillis());

        final SampleData message = SampleData.newBuilder()
                .withInfo(consumerRecord.value())
                .withKey(messageIdentificationKey)
                .withOffset(String.valueOf(consumerRecord.offset()))
                .withReceivedTimestamp(String.valueOf(consumerRecord.timestamp()))
                .withPartition(String.valueOf(consumerRecord.partition()))
                .build();

        final Record record = new Record(consumerRecord.topic(), acknowledgment, message);

        akkaSystem.processRecord(record);

        /*
        repository.save(SampleData.newBuilder()
                .withInfo(consumerRecord.value())
                .withKey(messageIdentificationKey)
                .withOffset(String.valueOf(consumerRecord.offset()))
                .withReceivedTimestamp(String.valueOf(consumerRecord.timestamp()))
                .withPartition(String.valueOf(consumerRecord.partition()))
                .build());
         */
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("Running Consumer's post construct...");
    }
}
