package mob.poc.akka.spring.app.akka.contract;

import mob.poc.akka.spring.app.model.SampleData;

public class Message implements Command {
    private final SampleData data;

    public Message(SampleData data) {
        this.data = data;
    }

    public SampleData getData() {
        return data;
    }
}
