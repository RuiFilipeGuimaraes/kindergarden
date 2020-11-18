package mob.poc.akka.spring.app.factory.impl;

import mob.poc.akka.spring.app.factory.api.Factory;
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
