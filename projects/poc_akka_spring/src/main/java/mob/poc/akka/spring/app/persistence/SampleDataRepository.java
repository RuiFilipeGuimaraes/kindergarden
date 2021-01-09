package mob.poc.akka.spring.app.persistence;

import mob.poc.akka.spring.app.model.SampleData;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SampleDataRepository {
    void save(SampleData sampleData);

    List<SampleData> retrieveByPartition(String partition);

}
