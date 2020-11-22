package mob.poc.akka.spring.app.akka.actor.handler;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;

import java.time.Duration;

import static akka.actor.SupervisorStrategy.makeDecider;

public class MessageHandlerSupervisor extends BaseActor {

    private static int MAX_NR_RETRIES = 10;
    private ActorRef childRef;

    public MessageHandlerSupervisor(int partition) {
        this.childRef = getContext().actorOf(MessageHandlerActor.props(partition), String.format("MessageHandler%s", partition));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Command.class, this::onCommand)
                .matchAny(message -> {
                    log().info(String.format("I am %s. Forwarding message to child. childRef=%s, childPath=%s, message=%s",
                            getContext().getSelf().path().name(), childRef, childRef.path().name(), message));
                    childRef.forward(message, getContext());
                })
                .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                MAX_NR_RETRIES,
                Duration.ofSeconds(10),
                makeDecider(ex -> SupervisorStrategy.Restart$.MODULE$)
        );
    }

    public static Props props(final int partition) {
        return Props.create(MessageHandlerSupervisor.class, partition);
    }

    @Override
    protected HealthStatus checkHealth() {
        return HealthStatus.HEALTHY;
    }

    @Override
    protected void forwardIfNeeded(Command command) {
        childRef.forward(command, getContext());
    }
}
