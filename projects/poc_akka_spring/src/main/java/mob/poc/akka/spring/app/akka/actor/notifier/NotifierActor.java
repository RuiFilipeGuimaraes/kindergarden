package mob.poc.akka.spring.app.akka.actor.notifier;

import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;

public class NotifierActor extends BaseActor {
    @Override
    protected HealthStatus checkHealth() {
        return HealthStatus.HEALTHY;
    }

    @Override
    public Receive createReceive() {
        return baseReceiveBuilder()
                .matchAny(m -> {
                    log().info("Received an unknown message type");
                })
                .build();
    }

    @Override
    protected void onForwardingResult(OperationResult result) {
        log().info("Notifying for result: {}", result.getType().name());
        getSender().tell(result, self());
    }

    public static Props props() {
        return Props.create(NotifierActor.class);
    }

}
