package my.postal.codes.app.domain.internal;

import com.google.common.base.Objects;
import org.apache.logging.log4j.util.Strings;

import static com.google.common.base.Preconditions.checkArgument;

public class Address {
    private String addressName;
    private Double distanceToHeathrowKilometers;
    private Double distanceToHeathrowMiles;
    private String latitude;
    private String longitude;

    private Address() {
    }

    private Address(String addressName, Double distanceToHeathrowKilometers, Double distanceToHeathrowMiles, String latitude, String longitude) {
        this.addressName = addressName;
        this.distanceToHeathrowKilometers = distanceToHeathrowKilometers;
        this.distanceToHeathrowMiles = distanceToHeathrowMiles;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String addressName;
        private Double distanceToHeathrowKilometers;
        private Double distanceToHeathrowMiles;
        private String latitude;
        private String longitude;

        public Address build() {
            checkArgument(Strings.isNotBlank(addressName), String.format("addressName is null or empty. addressName=%s", addressName));
            checkArgument(distanceToHeathrowKilometers != null, "distanceToHeathrowKilometers is null");
            checkArgument(distanceToHeathrowMiles != null, "distanceToHeathrowMiles is null");
            checkArgument(Strings.isNotBlank(latitude),
                    String.format("latitude is null or empty. latitude=%s", latitude));
            checkArgument(Strings.isNotBlank(longitude),
                    String.format("longitude is null or empty. longitude=%s", longitude));

            return new Address(addressName,
                    distanceToHeathrowKilometers,
                    distanceToHeathrowMiles,
                    latitude,
                    longitude);
        }

        public Builder withAddressName(final String addressName) {
            this.addressName = addressName;
            return this;
        }

        public Builder withDistanceToHeathrowKilometers(final Double distanceToHeathrowKilometers) {
            this.distanceToHeathrowKilometers = distanceToHeathrowKilometers;
            return this;
        }

        public Builder withDistanceToHeathrowMiles(final Double distanceToHeathrowMiles) {
            this.distanceToHeathrowMiles = distanceToHeathrowMiles;
            return this;
        }

        public Builder withLatitude(final String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final String longitude) {
            this.longitude = longitude;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equal(addressName, address.addressName) &&
                Objects.equal(distanceToHeathrowKilometers, address.distanceToHeathrowKilometers) &&
                Objects.equal(distanceToHeathrowMiles, address.distanceToHeathrowMiles) &&
                Objects.equal(latitude, address.latitude) &&
                Objects.equal(longitude, address.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(addressName, distanceToHeathrowKilometers, distanceToHeathrowMiles, latitude, longitude);
    }
}
