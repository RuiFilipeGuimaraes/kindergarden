package mob.poc.akka.spring.app.akka.actor.notifier;

import akka.actor.Props;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
import mob.poc.akka.spring.app.akka.actor.result.SuccessFulOperationResult;
import mob.poc.akka.spring.app.model.SampleData;

public class NotifierActor extends BaseActor {
    @Override
    protected HealthStatus checkHealth() {
        return HealthStatus.HEALTHY;
    }

    @Override
    public Receive createReceive() {
        return baseReceiveBuilder()
                .match(SampleData.class, this::onSampleData)
                .build();
    }


    private void onSampleData(SampleData data) {
        final OperationResult result = new SuccessFulOperationResult(data.getKey());
        getSender().tell(result, self());
    }

    @Override
    protected void onForwardingResult(OperationResult result) {
        getSender().tell(result, self());
    }

    public static Props props(final int partition) {
        return Props.create(NotifierActor.class);
    }

}
