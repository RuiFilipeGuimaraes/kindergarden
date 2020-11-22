package mob.poc.akka.spring.app.integration.akka;

import mob.poc.akka.spring.app.model.Record;

import java.util.concurrent.CompletableFuture;

public interface AkkaSystem {
    CompletableFuture<Object> processRecord(Record record);
}
