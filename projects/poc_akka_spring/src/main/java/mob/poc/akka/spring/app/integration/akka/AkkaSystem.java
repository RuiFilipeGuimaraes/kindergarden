package mob.poc.akka.spring.app.integration.akka;

import mob.poc.akka.spring.app.model.Record;

public interface AkkaSystem {
    void processRecord(Record record);
}
