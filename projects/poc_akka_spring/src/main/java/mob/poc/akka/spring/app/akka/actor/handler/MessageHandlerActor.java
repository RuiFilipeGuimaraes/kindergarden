package mob.poc.akka.spring.app.akka.actor.handler;

import akka.actor.ActorRef;
import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.persistence.MessagePersistenceSupervisorActor;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
import mob.poc.akka.spring.app.model.SampleData;

public class MessageHandlerActor extends BaseActor {

    private int counter = 0;
    private ActorRef childRef;

    public MessageHandlerActor(final int partition) {
        this.childRef = getContext().actorOf(MessagePersistenceSupervisorActor.props(partition),
                String.format("MessagePersistence%s", partition));
    }

    @Override
    public Receive createReceive() {
        return baseReceiveBuilder()
                .match(Message.class, this::onMessage)
                .match(SampleData.class, this::onMessage)
                .match(Command.class, this::onCommand)
                .matchAny(this::onDefaultMessage)
                .build();
    }

    private void onMessage(Object message) {
        log().info(String.format("I am %s and just received a message: %s", getContext().getSelf().path().name(), message));
        counter++;
        log().info("Increased Counter " + counter);
        log().info(String.format("Forwarding message to child: %s", childRef));
        childRef.forward(message, getContext());
    }

    private void onDefaultMessage(Object messageObject) {
        log().warning("Received an unknown message type!");
    }

    @Override
    protected void onForwardingResult(OperationResult result) {
        childRef.forward(result, getContext());
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
