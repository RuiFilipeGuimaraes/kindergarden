package mob.poc.akka.spring.app.akka.actor.persistence;

import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;

import java.util.ArrayList;
import java.util.List;

public class MessagePersistenceActor extends BaseActor {

    private final int partition;
    private List<Message> messages;

    public MessagePersistenceActor(final int partition) {
        this.partition = partition;
        this.messages = new ArrayList<>();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, this::onMessage)
                .match(Command.class, this::onCommand)
                .build();
    }

    private void onMessage(Message message) {
        log().info(String.format("I am %s and just received a message: %s", getContext().getSelf().path().name(), message));

        log().info("Persisting message!");
        messages.add(message);
    }

    public static Props props(final int partition) {
        return Props.create(MessagePersistenceActor.class, partition);
    }

    /**
     * Checks health status based on the connection to the DB
     *
     * @return HEALTHY if connection can be established, UNHEALTHY otherwise
     */
    @Override
    protected HealthStatus checkHealth() {
        //TODO check connection
        return HealthStatus.HEALTHY;
    }
}
