package my.postal.codes.app.calculator.distance.impl;

import my.postal.codes.app.calculator.distance.api.Coordinate;
import my.postal.codes.app.calculator.distance.api.DistanceCalculator;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class DefaultDistanceCalculator implements DistanceCalculator {

    @Override
    public double distanceInKilometers(Coordinate a, Coordinate b) {
        checkArgument(a != null, "a is null");
        checkArgument(b != null, "a is null");
        return calculateDistanceInKilometers(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
    }

    @Override
    public double distanceInMiles(Coordinate a, Coordinate b) {
        return this.distanceInKilometers(a, b) * 1.609344;
    }

    private double calculateDistanceInKilometers(final double lat1, final double lon1, final double lat2, final double lon2) {
        if (lat1 == lat2 && lon1 == lon2) {
            return 0;
        }

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }
}
