package mob.poc.akka.spring.app.akka.actor;

import akka.actor.*;
import org.springframework.context.ApplicationContext;

public class AkkaSpringExtension extends AbstractExtensionId<AkkaSpringExtension.SpringExt> {

    public static final AkkaSpringExtension AKKA_SPRING_EXTENSION_PROVIDER = new AkkaSpringExtension();

    private AkkaSpringExtension(){}

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static class SpringExt implements Extension{
        private volatile ApplicationContext applicationContext;

        public void initialize(ApplicationContext applicationContext){
            this.applicationContext = applicationContext;
        }

        public Props props(Class<? extends Actor> actorBeanType){
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanType);
        }
    }
}
