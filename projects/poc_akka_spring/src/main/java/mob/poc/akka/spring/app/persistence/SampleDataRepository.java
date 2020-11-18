package mob.poc.akka.spring.app.persistence;

import mob.poc.akka.spring.app.model.SampleData;

import java.util.Optional;

public interface SampleDataRepository {
    void add(SampleData sampleData);

    Optional<SampleData> retrieve(String key);

}
