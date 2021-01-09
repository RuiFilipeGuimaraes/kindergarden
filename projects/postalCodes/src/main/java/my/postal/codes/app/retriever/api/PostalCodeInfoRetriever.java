package my.postal.codes.app.retriever.api;

import my.postal.codes.app.domain.external.PostalCodeSearchResponse;

import java.util.Optional;

public interface PostalCodeInfoRetriever {

    Optional<PostalCodeSearchResponse> retrieveInfo(String postalCode);

}
