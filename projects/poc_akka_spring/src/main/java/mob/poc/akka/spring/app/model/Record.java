package mob.poc.akka.spring.app.model;

import org.springframework.kafka.support.Acknowledgment;

public class Record {
    private final Acknowledgment acknowledgment;
    private final SampleData data;
    private final String key;
    private final String topic;
    private final String partition;
    private final String offset;

    public Record(String topic, String offset, Acknowledgment acknowledgment, SampleData data) {
        this.acknowledgment = acknowledgment;
        this.data = data;
        this.key = String.format("K_%s_%s_%s", data.getPartition(), data.getSequenceNumber(), System.currentTimeMillis());
        this.partition = data.getPartition();
        this.offset = offset;
        this.topic = topic;
    }

    public Acknowledgment getAcknowledgment() {
        return acknowledgment;
    }

    public SampleData getData() {
        return data;
    }

    public String getKey() {
        return key;
    }

    public String getOffsetInfo() {
        return String.format("offset: %s, partition: %s, topic %s", offset, partition, topic);
    }

}
