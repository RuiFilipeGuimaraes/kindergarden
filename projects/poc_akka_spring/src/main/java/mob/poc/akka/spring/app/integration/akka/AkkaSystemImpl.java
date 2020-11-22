package mob.poc.akka.spring.app.integration.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.handler.MessageHandlerSupervisor;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
import mob.poc.akka.spring.app.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.ask;

@Component
public class AkkaSystemImpl implements AkkaSystem {

    private final Logger logger = LoggerFactory.getLogger(AkkaSystemImpl.class);

    private final Map<Integer, ActorRef> messageHandlerMap;
    private final ActorSystem actorSystem;
    private final Map<String, Record> recordsOnTransitMap;

    public AkkaSystemImpl() {
        logger.info("Initializing AKKA system...");
        this.actorSystem = ActorSystem.create("myActorSystem");
        this.messageHandlerMap = new HashMap<>();
        this.recordsOnTransitMap = new HashMap<>();
    }

    @Override
    public void processRecord(Record record) {
        logger.info("Processing record");
        final ActorRef handler = getOrCreateHandlerForPartition(Integer.valueOf(record.getData().getPartition()), actorSystem);

        this.recordsOnTransitMap.put(record.getKey(), record);

        CompletableFuture<Object> future = ask(handler, record.getData(), Duration.ofSeconds(30)).toCompletableFuture();
        future.whenComplete((result, throwable) -> {
            if (result instanceof OperationResult) {
                if (((OperationResult) result).isSuccess()) {
                    final Record processedRecord = recordsOnTransitMap.get(((OperationResult) result).getMessageKey());
                    logger.info("OFFSET COMMIT. {} ", record.getOffsetInfo());
                    processedRecord.getAcknowledgment().acknowledge();
                }
            } else {
                logger.error("Received a result that is not of the supporte type");
            }
        });

    }

    private ActorRef getOrCreateHandlerForPartition(final Integer partitionNumber, final ActorSystem actorSystem) {
        if (messageHandlerMap.containsKey(partitionNumber)) {
            return messageHandlerMap.get(partitionNumber);
        } else {
            final ActorRef newHandler = actorSystem.actorOf(Props.create(MessageHandlerSupervisor.class, partitionNumber),
                    String.format("SupervisorHandlerPartition%s", partitionNumber));
            messageHandlerMap.put(partitionNumber, newHandler);
            return newHandler;
        }
    }
}
