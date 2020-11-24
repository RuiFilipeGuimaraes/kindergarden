package mob.poc.akka.spring.app.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.handler.MessageHandlerSupervisor;
import mob.poc.akka.spring.app.akka.actor.AkkaSpringExtension;
import mob.poc.akka.spring.app.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
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

    public AkkaSystemImpl(ApplicationContext applicationContext) {
        logger.info("Initializing AKKA system...");
        this.actorSystem = ActorSystem.create("myActorSystem");
        AkkaSpringExtension.AKKA_SPRING_EXTENSION_PROVIDER.get(actorSystem).initialize(applicationContext);
        this.messageHandlerMap = new HashMap<>();
    }

    @Override
    public CompletableFuture<Object> processRecord(Record record) {
        logger.info("Processing record");
        final ActorRef handler = getOrCreateHandlerForPartition(Integer.valueOf(record.getData().getPartition()), actorSystem);
        return ask(handler, record.getData(), Duration.ofSeconds(30)).toCompletableFuture();
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
