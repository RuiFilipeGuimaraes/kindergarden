package mob.poc.akka.spring.app.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SampleData {
    private String info;
    private int sequenceNumber;
    private String partition;
    private String offset;
    private String receivedTimestamp;
    private String key;

    public SampleData(String key, String info) {
        this.key = key;
        this.info = info;
    }

    public SampleData(String info, int sequenceNumber, String partition, String offset, String receivedTimestamp, String key) {
        this.info = info;
        this.sequenceNumber = sequenceNumber;
        this.partition = partition;
        this.offset = offset;
        this.receivedTimestamp = receivedTimestamp;
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleData that = (SampleData) o;
        return sequenceNumber == that.sequenceNumber &&
                Objects.equal(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(info, sequenceNumber);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("info", info)
                .add("sequenceNumber", sequenceNumber)
                .toString();
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public String getPartition() {
        return partition;
    }

    public static class Builder {
        private String info;
        private int sequenceNumber;
        private String partition;
        private String offset;
        private String receivedTimestamp;
        private String key;

        public Builder() {
        }

        public Builder withInfo(final String info) {
            this.info = info;
            return this;
        }

        public Builder withSequenceNumber(final int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public Builder withPartition(final String partition) {
            this.partition = partition;
            return this;
        }

        public Builder withOffset(final String offset) {
            this.offset = offset;
            return this;
        }

        public Builder withReceivedTimestamp(final String receivedTimestamp) {
            this.receivedTimestamp = receivedTimestamp;
            return this;
        }

        public Builder withKey(final String key) {
            this.key = key;
            return this;
        }

        public SampleData build() {
            return new SampleData(info, sequenceNumber, partition, offset, receivedTimestamp, key);
        }
    }

}
