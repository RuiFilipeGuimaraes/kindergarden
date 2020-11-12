package my.postal.codes.app.persistence.impl;

import com.google.gson.Gson;
import my.postal.codes.app.persistence.api.SearchHistoryRepository;
import my.postal.codes.app.persistence.api.SearchOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Repository
public class RedisSearchHistoryRepository implements SearchHistoryRepository {

    private final Jedis jedis;
    private final Gson serializer;

    public RedisSearchHistoryRepository(@Autowired final Jedis jedis, @Autowired final Gson gson) {
        checkArgument(jedis != null, "jedis is null");
        checkArgument(gson != null, "gson is null");
        this.jedis = jedis;
        this.serializer = gson;
    }

    @Override
    public void addSearchOperation(final SearchOperation searchOperation) {
        checkArgument(searchOperation != null, "searchOperation is null");
        jedis.lpush(searchOperation.getUserId(), serializer.toJson(searchOperation));
    }

    @Override
    public List<SearchOperation> retrieveSearchHistoryForUser(final String userId, final int maxNumberOfRecords) {
        checkArgument(Strings.isNotBlank(userId), "userId is null or empty");
        checkArgument(maxNumberOfRecords >= 0,
                String.format("invalid non positive maxNumberOfRecords. maxNumberOfRecords=%s", maxNumberOfRecords));
        if (maxNumberOfRecords == 0) {
            return new ArrayList<>();
        }
        Function<String, SearchOperation> toSearchOperation = json -> serializer.fromJson(json, SearchOperation.class);

        final long listOfOperationsSize = jedis.llen(userId);
        final long startIndex = -1*listOfOperationsSize + 1;
        final long lastIndex = -1*listOfOperationsSize + maxNumberOfRecords;


        return jedis.lrange(userId, startIndex, lastIndex).stream()
                .map(toSearchOperation)
                .collect(Collectors.toList());
    }

}
