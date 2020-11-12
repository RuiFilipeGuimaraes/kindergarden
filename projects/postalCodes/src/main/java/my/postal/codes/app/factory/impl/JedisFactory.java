package my.postal.codes.app.factory.impl;

import my.postal.codes.app.factory.api.Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisFactory implements Factory<Jedis> {
    @Override
    @Bean(name = "produceJedis")
    public Jedis produce() {
        return new Jedis();
    }
}
