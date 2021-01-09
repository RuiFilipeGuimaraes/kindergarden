package my.postal.codes.app.processor.impl;

import my.postal.codes.app.converter.api.PostalCodeInfoConverter;
import my.postal.codes.app.domain.external.PostalCodeSearchResponse;
import my.postal.codes.app.domain.internal.Address;
import my.postal.codes.app.domain.internal.AddressWithSearchHistory;
import my.postal.codes.app.domain.internal.RequestInformation;
import my.postal.codes.app.enricher.api.DataEnricher;
import my.postal.codes.app.exception.NoPostalCodeInfoAvailableException;
import my.postal.codes.app.history.updater.api.SearchHistoryUpdater;
import my.postal.codes.app.processor.api.RequestProcessor;
import my.postal.codes.app.retriever.api.PostalCodeInfoRetriever;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class PostalCodeSearchProcessor implements RequestProcessor<RequestInformation, Optional<AddressWithSearchHistory>> {

    private final PostalCodeInfoRetriever postalCodeInfoRetriever;
    private final PostalCodeInfoConverter postalCodeInfoConverter;
    private final DataEnricher<Address, AddressWithSearchHistory> searchHistoryDataEnricher;
    private final SearchHistoryUpdater searchHistoryUpdater;
    private Supplier<RuntimeException> exceptionBecauseNoPostalCodeInfoIsAvailable = () -> new NoPostalCodeInfoAvailableException("No postal code info found");

    public PostalCodeSearchProcessor(@Autowired final PostalCodeInfoRetriever postalCodeInfoRetriever,
                                     @Autowired final PostalCodeInfoConverter postalCodeInfoConverter,
                                     @Autowired final DataEnricher<Address, AddressWithSearchHistory> searchHistoryDataEnricher,
                                     @Autowired final SearchHistoryUpdater searchHistoryUpdater) {
        checkArgument(postalCodeInfoConverter != null, "postalCodeInfoConverter is null");
        checkArgument(searchHistoryDataEnricher != null, "searchHistoryDataEnricher is null");
        checkArgument(postalCodeInfoRetriever != null, "postalCodeInfoRetriever is null");
        checkArgument(searchHistoryUpdater != null, "searchHistoryUpdater is null");
        this.postalCodeInfoConverter = postalCodeInfoConverter;
        this.searchHistoryDataEnricher = searchHistoryDataEnricher;
        this.postalCodeInfoRetriever = postalCodeInfoRetriever;
        this.searchHistoryUpdater = searchHistoryUpdater;
    }

    @Override
    public Optional<AddressWithSearchHistory> processRequest(RequestInformation requestInformation) {
        checkArgument(requestInformation != null, "requestInformation is null");
        checkArgument(Strings.isNotBlank(requestInformation.getPostalCode()),
                String.format("postalCode is null or blank. requestInformation.postalCode=%s", requestInformation.getPostalCode()));
        checkArgument(Strings.isNotBlank(requestInformation.getUserId()),
                String.format("userId is null or blank. requestInformation.userId=%s", requestInformation.getUserId()));

        Function<RequestInformation, PostalCodeSearchResponse> toPostalCodeInformationRetrieved =
                info -> postalCodeInfoRetriever.retrieveInfo(requestInformation.getPostalCode())
                        .orElseThrow(exceptionBecauseNoPostalCodeInfoIsAvailable);

        Function<PostalCodeSearchResponse, Address> toAddress = postalCodeInfoConverter::convertToAddress;
        Function<Address, AddressWithSearchHistory> toAddressEnrichedWithSearchHistory = address -> searchHistoryDataEnricher.enrich(address, requestInformation.getUserId());
        Consumer<AddressWithSearchHistory> updateSearchHistoryForUser = addressWithSearchHistory ->
                searchHistoryUpdater.updateSearchHistory(addressWithSearchHistory.getAddress(), requestInformation.getUserId());

        return Stream.of(requestInformation)
                .map(toPostalCodeInformationRetrieved)
                .map(toAddress)
                .map(toAddressEnrichedWithSearchHistory)
                .peek(updateSearchHistoryForUser)
                .findFirst();
    }

}
