package mob.poc.akka.spring.app.persistence;

import com.google.gson.Gson;
import mob.poc.akka.spring.app.model.SampleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Adds the data to the list of messages for the same partition, assuring that writing order is maintained
     *
     * @param sampleData
     */
    @Override
    public void save(SampleData sampleData) {
        jedis.lpush(sampleData.getPartition(), serializer.toJson(sampleData));
    }

    @Override
    public List<SampleData> retrieveByPartition(String partition) {
        final long length = jedis.llen(partition);
        return jedis.lrange(partition, 0, length)
                .parallelStream()
                .map(str -> serializer.fromJson(str, SampleData.class))
                .collect(Collectors.toList());
    }
}
