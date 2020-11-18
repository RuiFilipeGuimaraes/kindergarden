package mob.poc.akka.spring.app.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SampleData {
    private String info;
    private int sequenceNumber;

    public SampleData(String info, int sequenceNumber) {
        this.info = info;
        this.sequenceNumber = sequenceNumber;
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
}
