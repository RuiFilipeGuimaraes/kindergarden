package mob.poc.akka.spring.app.akka.actor.result;

import java.util.Optional;

public interface OperationResult {
    ResultType getType();

    String getMessageKey();

    Optional<Throwable> getThrowable();

    default boolean isSuccess() {
        return getType().equals(ResultType.SUCCESS);
    }
}
