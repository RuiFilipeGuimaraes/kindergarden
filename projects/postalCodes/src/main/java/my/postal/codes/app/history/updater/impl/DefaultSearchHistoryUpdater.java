package my.postal.codes.app.history.updater.impl;

import com.google.common.base.Preconditions;
import my.postal.codes.app.domain.internal.Address;
import my.postal.codes.app.history.updater.api.SearchHistoryUpdater;
import my.postal.codes.app.persistence.api.SearchHistoryRepository;
import my.postal.codes.app.persistence.api.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSearchHistoryUpdater implements SearchHistoryUpdater {

    private SearchHistoryRepository searchHistoryRepository;

    public DefaultSearchHistoryUpdater(@Autowired final SearchHistoryRepository searchHistoryRepository) {
        Preconditions.checkArgument(searchHistoryRepository != null, "searchHistoryRepository is null");
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public void updateSearchHistory(Address address, String userId) {
        searchHistoryRepository.addSearchOperation(new SearchOperation(userId, address, System.currentTimeMillis()));
    }
}
