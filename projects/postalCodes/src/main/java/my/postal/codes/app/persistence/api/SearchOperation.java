package my.postal.codes.app.persistence.api;

import com.google.common.base.Objects;
import my.postal.codes.app.domain.internal.Address;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

public class SearchOperation implements Comparable<SearchOperation> {

    private final String userId;
    private final Address address;
    private final Long timestamp;

    public SearchOperation(final String userId,
                           final Address address,
                           final long timestamp) {
        checkArgument(Strings.isNotBlank(userId),
                String.format("userId is null or empty. userId=%s", userId));
        checkArgument(address != null, "address is null");
        checkArgument(timestamp != 0L, "timestamp is 0 (invalid value)");
        this.userId = userId;
        this.address = address;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public Address getAddress() {
        return address;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchOperation that = (SearchOperation) o;
        return Objects.equal(userId, that.userId) &&
                Objects.equal(address, that.address) &&
                Objects.equal(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, address, timestamp);
    }

    @Override
    public int compareTo(@Nonnull SearchOperation anotherOperation) {

        if (this.timestamp < anotherOperation.timestamp) {
            return -1;
        } else if (this.timestamp.equals(anotherOperation.timestamp)) {
            return 0;
        } else {
            return 1;
        }
    }
}
