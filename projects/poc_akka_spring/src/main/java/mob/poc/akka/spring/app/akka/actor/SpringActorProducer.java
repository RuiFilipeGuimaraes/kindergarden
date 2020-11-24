package mob.poc.akka.spring.app.akka.actor;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import mob.poc.akka.spring.app.akka.actor.config.Key;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class SpringActorProducer implements IndirectActorProducer {

    private ApplicationContext applicationContext;
    private Class<? extends Actor> beanType;
    private final Map<Key, Object> beanConfiguration;

    public SpringActorProducer(ApplicationContext applicationContext, Class<? extends Actor> beanType) {
        this.applicationContext = applicationContext;
        this.beanType = beanType;
        this.beanConfiguration = new HashMap<>();
    }

    public SpringActorProducer(ApplicationContext applicationContext,
                               Class<? extends Actor> beanType,
                               Map<Key, Object> beanConfiguration) {
        this.applicationContext = applicationContext;
        this.beanType = beanType;
        this.beanConfiguration = beanConfiguration;
    }

    @Override
    public Actor produce() {
        return applicationContext.getBean(beanType, beanConfiguration);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return beanType;
    }

}
