package mob.poc.akka.spring.app.akka.actor.persistence;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.gson.Gson;
import mob.poc.akka.spring.app.akka.actor.BaseActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.contract.Message;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;
import mob.poc.akka.spring.app.akka.actor.notifier.NotifierActor;
import mob.poc.akka.spring.app.akka.actor.result.FailureOperationResult;
import mob.poc.akka.spring.app.akka.actor.result.OperationResult;
import mob.poc.akka.spring.app.akka.actor.result.SuccessFulOperationResult;
import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.RedisSampleDataRepository;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class MessagePersistenceActor extends BaseActor {

    private final int partition;
    private List<Message> messages;
    private ActorRef childRef;

    //TODO inject this dependency with spring
    private SampleDataRepository repository = new RedisSampleDataRepository(new Jedis(), new Gson());

    public MessagePersistenceActor(final int partition) {
        this.partition = partition;
        this.messages = new ArrayList<>();
        this.childRef = getContext().actorOf(NotifierActor.props(), String.format("Notifier%s", partition));
    }

    @Override
    public Receive createReceive() {
        return baseReceiveBuilder()
                .match(Message.class, this::onMessage)
                .match(SampleData.class, this::onSampleData)
                .match(Command.class, this::onCommand)
                .build();
    }

    private void onSampleData(SampleData sampleData) {
        final OperationResult result = persist(sampleData);
        childRef.forward(result, getContext());
    }

    private OperationResult persist(SampleData sampleData) {
        try {
            //throw new Exception("JUST FAILED");
            repository.save(sampleData);
            log().info("Data successfully saved in the DB. key={}", sampleData.getKey());
            return new SuccessFulOperationResult(sampleData.getKey());
        } catch (Exception e) {
            log().error("Failed persisting data. key={}", sampleData.getKey());
            return new FailureOperationResult(sampleData.getKey(), e);
        }
    }

    private void onMessage(Message message) {
        log().info(String.format("I am %s and just received a message: %s", getContext().getSelf().path().name(), message));

        log().info("Persisting message!");
        messages.add(message);
    }

    @Override
    protected void onForwardingResult(OperationResult result) {
        childRef.forward(result, getContext());
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
