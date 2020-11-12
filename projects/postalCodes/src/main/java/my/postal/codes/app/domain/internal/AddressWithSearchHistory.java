package my.postal.codes.app.domain.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

public class AddressWithSearchHistory {
    final Address address;
    final List<Address> searchHistory;

    public AddressWithSearchHistory(Address address, List<Address> searchHistory) {
        this.address = address;
        this.searchHistory = searchHistory;
    }

    public Address getAddress() {
        return address;
    }

    public List<Address> getSearchHistory() {
        return searchHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressWithSearchHistory that = (AddressWithSearchHistory) o;
        return Objects.equal(address, that.address) &&
                Objects.equal(searchHistory, that.searchHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address, searchHistory);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("address", address)
                .add("searchHistory", searchHistory)
                .toString();
    }
}
