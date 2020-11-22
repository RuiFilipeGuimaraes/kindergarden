package mob.poc.akka.spring.app.akka.actor;

import akka.actor.AbstractLoggingActor;
import mob.poc.akka.spring.app.akka.actor.contract.Command;
import mob.poc.akka.spring.app.akka.actor.domain.HealthStatus;

public abstract class BaseActor extends AbstractLoggingActor {

    protected void onCommand(Command command) {
        switch (command) {
            case HEALTHCHECK:
                logHealth();
                forwardIfNeeded(command);
                break;
            default:
                break;
        }
    }

    protected void logHealth() {
        log().info(String.format("I am: %s. HealthStatus: %s",
                getSelf(),
                checkHealth().name()));
    }

    protected abstract HealthStatus checkHealth();

    protected void forwardIfNeeded(Command command) {
    }
}
