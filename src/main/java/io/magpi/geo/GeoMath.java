package io.magpi.geo;

import static java.lang.Math.*;

public class GeoMath {
/*

REF:    http://cdn.rawgit.com/chrisveness/geodesy/v1.1.1/latlon-spherical.js

*/
    private static final Double R=6371e3;

    public static Coordinate destinationPoint(Coordinate startPoint, Double bearing, Double distance) {

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

        return new Coordinate( toDegrees(φ2), (toDegrees(λ2)+540)%360-180);

    }



    public static Grid grid(Coordinate center, Double bearing, Double distance,Integer samples) {

        Coordinate gridStart= GeoMath.gridStart(center,bearing,distance);

        Double gridLineOffset=distance/samples;

        Grid grid=new Grid();
        Coordinate rowStart=gridStart;
        for(int i=0;i<samples;i++){


            Path row=gridRow(rowStart,gridLineOffset,bearing,samples);



            grid.add(row);
            rowStart=nextRowStart(rowStart,gridLineOffset,bearing);
        }


        return grid;
    }

    private static Coordinate nextRowStart(Coordinate rowStart, Double gridLineOffset, Double bearing) {
        Double perpendicularBearing=bearing+270%360;

        return destinationPoint(rowStart,perpendicularBearing,gridLineOffset);
    }

    private static Path gridRow(Coordinate rowStart, Double gridLineOffset, Double bearing, Integer samples) {
        Path path=new Path();
        Coordinate next=rowStart;
        path.add(next);
        for(int i=1;i<samples;i++){

            next=destinationPoint(next,bearing,gridLineOffset);
            path.add(next);

        }

        return path;
    }

    private static Coordinate gridStart(Coordinate center, Double bearing, Double distance) {

        Double distanceToCorner=sqrt(distance*distance+distance*distance)/2;
        Double angleToCorner=bearing+145d%360;

        return destinationPoint(center,angleToCorner,distanceToCorner);


    }
}
