package mob.poc.akka.spring.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.handler.MessageHandlerSupervisor;

import java.util.*;
import java.util.stream.Stream;

public class AkkaAppTest {

    private static Map<Integer, ActorRef> messageHandlerMap = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("myActorSystem");

        final Set<Integer> partitions = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));

        partitions.forEach(partitionNumber -> {
            Stream.of("Message 1", "Message 2", "Message 3").forEach(m -> {
                final ActorRef handler = getOrCreateHandlerForPartition(partitionNumber, actorSystem);
                handler.tell(new Message(m), ActorRef.noSender());
            });
        });

        Thread.sleep(2000);
        //check health status of the system
        messageHandlerMap.values().forEach(supervisor -> supervisor.tell(Command.HEALTHCHECK, ActorRef.noSender()));

        actorSystem.terminate();
    }

    private static ActorRef getOrCreateHandlerForPartition(final Integer partitionNumber, final ActorSystem actorSystem) {
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
