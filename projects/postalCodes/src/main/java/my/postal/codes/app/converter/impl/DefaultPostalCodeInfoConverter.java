package my.postal.codes.app.converter.impl;

import my.postal.codes.app.calculator.distance.api.Coordinate;
import my.postal.codes.app.calculator.distance.api.DistanceCalculator;
import my.postal.codes.app.converter.api.PostalCodeInfoConverter;
import my.postal.codes.app.domain.external.PostalCodeSearchResponse;
import my.postal.codes.app.domain.internal.Address;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class DefaultPostalCodeInfoConverter implements PostalCodeInfoConverter {

    private static final String SEPARATOR = " ";
    private final DistanceCalculator distanceCalculator;

    private final Coordinate heathrowAirportCoordinates = new Coordinate(51.4700223, -0.4542955);

    public DefaultPostalCodeInfoConverter(final DistanceCalculator distanceCalculator) {
        checkArgument(distanceCalculator != null, "distanceCalculator is null");
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public Address convertToAddress(final PostalCodeSearchResponse response) {
        checkArgument(response != null, "response is null");
        checkArgument(response.getResult() != null, "response.result is null");

        final Coordinate addressCoordinates = new Coordinate(Double.parseDouble(response.getResult().getLatitude()),
                Double.parseDouble(response.getResult().getLongitude()));

        return Address.newBuilder()
                .withAddressName(computeAddressName(response.getResult()))
                .withLatitude(response.getResult().getLatitude())
                .withLongitude(response.getResult().getLongitude())
                .withDistanceToHeathrowKilometers(distanceCalculator.distanceInKilometers(addressCoordinates, heathrowAirportCoordinates))
                .withDistanceToHeathrowMiles(distanceCalculator.distanceInMiles(addressCoordinates, heathrowAirportCoordinates))
                .build();
    }

    private String computeAddressName(final PostalCodeSearchResponse.PostalCodeSearchInformation info) {
        return info.getLsoa()
                .concat(SEPARATOR)
                .concat(info.getAdmin_ward())
                .concat(SEPARATOR)
                .concat(info.getPostcode())
                .concat(SEPARATOR)
                .concat(info.getCountry());
    }
}
