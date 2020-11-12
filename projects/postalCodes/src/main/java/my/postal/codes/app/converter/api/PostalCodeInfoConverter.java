package my.postal.codes.app.converter.api;

import my.postal.codes.app.domain.external.PostalCodeSearchResponse;
import my.postal.codes.app.domain.internal.Address;

public interface PostalCodeInfoConverter {

    Address convertToAddress(PostalCodeSearchResponse response);

}
