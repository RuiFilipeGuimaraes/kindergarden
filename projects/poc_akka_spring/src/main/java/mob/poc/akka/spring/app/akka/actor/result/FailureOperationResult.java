package mob.poc.akka.spring.app.akka.actor.result;

import java.util.Optional;

public class FailureOperationResult implements OperationResult {

    private final String messageKey;
    private final Throwable t;

    public FailureOperationResult(String messageKey, Throwable throwable) {
        this.messageKey = messageKey;
        this.t = throwable;
    }

    @Override
    public ResultType getType() {
        return ResultType.FAILURE;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public Optional<Throwable> getThrowable() {
        return Optional.of(t);
    }
}
