package my.postal.codes.app.persistence.api;

import java.util.List;

public interface SearchHistoryRepository {

    void addSearchOperation(final SearchOperation searchOperation);

    List<SearchOperation> retrieveSearchHistoryForUser(final String userId, final int maxNumberOfRecords);
}
