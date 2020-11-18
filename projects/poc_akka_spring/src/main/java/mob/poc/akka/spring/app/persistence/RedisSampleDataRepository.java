package mob.poc.akka.spring.app.persistence;

import com.google.gson.Gson;
import mob.poc.akka.spring.app.model.SampleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Repository
public class RedisSampleDataRepository implements SampleDataRepository {
    private final Jedis jedis;
    private final Gson serializer;

    public RedisSampleDataRepository(@Autowired final Jedis jedis, @Autowired final Gson gson) {
        checkArgument(jedis != null, "jedis is null");
        checkArgument(gson != null, "gson is null");
        this.jedis = jedis;
        this.serializer = gson;
    }

    @Override
    public void add(SampleData sampleData) {
        jedis.lpush(String.valueOf(sampleData.getSequenceNumber()), serializer.toJson(sampleData));
    }

    @Override
    public Optional<SampleData> retrieve(String key) {
        return Optional.ofNullable(jedis.get(key)).map(dataStr -> serializer.fromJson(dataStr, SampleData.class));
    }
}
