package mob.poc.akka.spring.app.akka.actor.handler;

import akka.actor.ActorRef;
import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.persistence.MessagePersistenceActor;

public class MessageHandlerActor extends BaseActor {

    private int counter = 0;
    private ActorRef childRef;

    public MessageHandlerActor(final int partition) {
        this.childRef = getContext().actorOf(MessagePersistenceActor.props(partition), String.format("MessagePersistence%s", partition));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, this::onMessage)
                .match(Command.class, this::onCommand)
                .matchAny(this::onDefaultMessage)
                .build();
    }

    private void onMessage(Message message) {
        log().info(String.format("I am %s and just received a message: %s", getContext().getSelf().path().name(), message));
        counter++;
        log().info("Increased Counter " + counter);
        log().info("Forwarding message to child");
        childRef.forward(message, getContext());
    }

    private void onDefaultMessage(Object messageObject) {
        log().warning("Received an unknown message type!");
    }

    /**
     * Factory method to create the actor
     */
    public static Props props(final int partition) {
        return Props.create(MessageHandlerActor.class, partition);
    }

    @Override
    protected HealthStatus checkHealth() {
        return HealthStatus.HEALTHY;
    }
}
