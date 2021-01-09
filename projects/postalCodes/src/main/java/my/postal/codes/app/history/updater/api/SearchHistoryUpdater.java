package my.postal.codes.app.history.updater.api;

import my.postal.codes.app.domain.internal.Address;

public interface SearchHistoryUpdater {
    void updateSearchHistory(Address address, String userId);
}
