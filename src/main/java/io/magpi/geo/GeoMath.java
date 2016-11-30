package io.magpi.geo;

import static java.lang.Math.*;

public class GeoMath {
/*

REF:    http://cdn.rawgit.com/chrisveness/geodesy/v1.1.1/latlon-spherical.js

*/
    private static final Double R=6371e3;
    public static Coordinate calcEndPoint(Coordinate startPoint, Double bearing, Integer distance) {

        Double δ = distance / R; // angular distance in radians
        Double θ = toRadians(bearing);

        Double φ1 = toRadians(startPoint.getLatitude());
        Double λ1 = toRadians(startPoint.getLongitude());

        Double sinφ1 = sin(φ1), cosφ1 = cos(φ1);
        Double sinδ = sin(δ), cosδ = cos(δ);
        Double sinθ = sin(θ), cosθ = cos(θ);

        Double sinφ2 = sinφ1*cosδ + cosφ1*sinδ*cosθ;
        Double φ2 = asin(sinφ2);
        Double y = sinθ * sinδ * cosφ1;
        Double x = cosδ - sinφ1 * sinφ2;
        Double λ2 = λ1 + atan2(y, x);

        return new Coordinate(toDegrees(φ2), (toDegrees(λ2)+540)%360-180);

    }
}
