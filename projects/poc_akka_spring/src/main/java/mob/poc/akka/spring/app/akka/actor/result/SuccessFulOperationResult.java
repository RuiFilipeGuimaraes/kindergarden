package mob.poc.akka.spring.app.akka.actor.result;

import java.util.Optional;

public class SuccessFulOperationResult implements OperationResult {

    private final String messageKey;

    public SuccessFulOperationResult(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public ResultType getType() {
        return ResultType.SUCCESS;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public Optional<Throwable> getThrowable() {
        return Optional.empty();
    }
}
