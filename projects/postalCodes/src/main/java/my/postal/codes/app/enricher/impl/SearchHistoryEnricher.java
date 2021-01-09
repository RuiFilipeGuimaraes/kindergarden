package my.postal.codes.app.enricher.impl;

import my.postal.codes.app.domain.internal.Address;
import my.postal.codes.app.domain.internal.AddressWithSearchHistory;
import my.postal.codes.app.enricher.api.DataEnricher;
import my.postal.codes.app.exception.UnableToEnrichWithSearchHistoryException;
import my.postal.codes.app.persistence.api.SearchHistoryRepository;
import my.postal.codes.app.persistence.api.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class SearchHistoryEnricher implements DataEnricher<Address, AddressWithSearchHistory> {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryEnricher(SearchHistoryRepository searchHistoryRepository) {
        checkArgument(searchHistoryRepository != null, "searchHistoryRepository is null");
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public AddressWithSearchHistory enrich(final Address addressToEnrich, final String userId) {
        checkArgument(addressToEnrich != null, "addressToEnrich is null");

        return Stream.of(addressToEnrich)
                .map(address -> searchHistoryRepository.retrieveSearchHistoryForUser(userId, 3))
                .map(searchOperations -> searchOperations.stream().map(SearchOperation::getAddress).collect(Collectors.toList()))
                .map(addresses -> new AddressWithSearchHistory(addressToEnrich, addresses))
                .findFirst()
                .orElseThrow(() -> new UnableToEnrichWithSearchHistoryException(
                        String.format("Unable to enrich the address with search history. addressToEnrich=%s", addressToEnrich)));
    }
}
