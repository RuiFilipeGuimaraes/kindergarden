package mob.poc.akka.spring.app.integration.kafka;

import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private NewTopic newTopic;

    @Autowired
    private AkkaSystemImpl akkaSystem;

    @Autowired
    private SampleDataRepository repository;

    private final Map<String, Record> recordsOnTransitMap = new HashMap<>();

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

        final Record record = new Record(consumerRecord.topic(), String.valueOf(consumerRecord.offset()), acknowledgment, message);
        this.recordsOnTransitMap.put(record.getKey(), record);

        CompletableFuture<Object> future = akkaSystem.processRecord(record);
        future.whenComplete((result, throwable) -> {
            if (result instanceof OperationResult) {
                if (((OperationResult) result).isSuccess()) {
                    final Record processedRecord = recordsOnTransitMap.get(((OperationResult) result).getMessageKey());
                    logger.info("OFFSET COMMIT. {} ", record.getOffsetInfo());
                    acknowledgment.acknowledge();
                }
            } else {
                logger.error("Received a result that is not of the supporte type");
            }
        });
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("Running Consumer's post construct...");
    }
}
