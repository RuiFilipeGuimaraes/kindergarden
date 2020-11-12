package my.postal.codes.app.calculator.distance.api;

public interface DistanceCalculator {

    double distanceInKilometers(Coordinate a, Coordinate b);

    double distanceInMiles(Coordinate a, Coordinate b);
}
