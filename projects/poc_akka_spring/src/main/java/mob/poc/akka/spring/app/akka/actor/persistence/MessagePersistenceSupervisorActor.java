package mob.poc.akka.spring.app.akka.actor.persistence;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
import mob.poc.akka.spring.app.model.SampleData;

import java.time.Duration;

import static akka.actor.SupervisorStrategy.makeDecider;
import static mob.poc.akka.spring.app.akka.actor.AkkaSpringExtension.AKKA_SPRING_EXTENSION_PROVIDER;

public class MessagePersistenceSupervisorActor extends BaseActor {
    private ActorRef childRef;
    private static int MAX_NR_RETRIES = 10;

    public MessagePersistenceSupervisorActor(int partition) {
        /*
        this.childRef = getContext().actorOf(MessagePersistenceActor.props(partition),
                String.format("MessagePersistence%s", partition));
         */

        this.childRef =getContext().actorOf(AKKA_SPRING_EXTENSION_PROVIDER.get(getContext().getSystem())
                        .props(MessagePersistenceActor.class), String.format("MessagePersistence%s", partition));
        /*
        this.childRef = getContext().actorOf(MessagePersistenceActor.props(partition).withDispatcher("blocking-dispatcher"),
                String.format("MessagePersistence%s", partition));

         */
    }

    @Override
    protected HealthStatus checkHealth() {
        return HealthStatus.HEALTHY;
    }

    @Override
    public Receive createReceive() {
        return baseReceiveBuilder()
                .match(Message.class, this::onMessage)
                .match(SampleData.class, this::onMessage)
                .build();
    }

    private void onMessage(Object message) {
        log().info(String.format("I am %s and just received a message: %s", getContext().getSelf().path().name(), message));
        log().info(String.format("Forwarding message to child: %s", childRef));
        childRef.forward(message, getContext());
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                MAX_NR_RETRIES,
                Duration.ofSeconds(10),
                makeDecider(ex -> SupervisorStrategy.Restart$.MODULE$)
        );
    }

    @Override
    protected void onForwardingResult(OperationResult result) {
        childRef.forward(result, getContext());
    }

    public static Props props(final int partition) {
        return Props.create(MessagePersistenceSupervisorActor.class, partition);
    }
}
