package com.dido.shipping.utils;

public final class DistanceCalculationUtils {

    private DistanceCalculationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double distance(double latitudeSource,
                                  double latitudeTarget, double longitudeSource,
                                  double longitudeTarget, DistanceType targetType) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        longitudeSource = Math.toRadians(longitudeSource);
        longitudeTarget = Math.toRadians(longitudeTarget);
        latitudeSource = Math.toRadians(latitudeSource);
        latitudeTarget = Math.toRadians(latitudeTarget);

        // Haversine formula
        double longitudeDistance = longitudeTarget - longitudeSource;
        double latitudeDistance = latitudeTarget - latitudeSource;
        double a = Math.pow(Math.sin(latitudeDistance / 2), 2)
                + Math.cos(latitudeSource) * Math.cos(latitudeTarget)
                * Math.pow(Math.sin(longitudeDistance / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        if (DistanceType.MILES == targetType) {
            r = 3956;
        }

        // calculate the result
        return (c * r);
    }

    public enum DistanceType {
        KM, MILES
    }
}
