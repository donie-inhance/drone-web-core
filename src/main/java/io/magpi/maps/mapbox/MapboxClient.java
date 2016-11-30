package io.magpi.maps.mapbox;

import io.magpi.geo.Coordinate;
import io.magpi.geo.Path;
import io.magpi.maps.MapProvider;
import io.vertx.core.Future;

public class MapboxClient implements MapProvider
{

    private static final String HOST ="https://api.mapbox.com";
    private static final String MAP = "";
    private static final String ACCESS_TOKEN="pk.eyJ1IjoicGF0cmljazExY29ud2F5IiwiYSI6ImNpdzR3djBiYjAwMHgyb3BjMHc0Y3p3YjAifQ.0MzVfaGqqRYaYUqqQRAvQg";





    @Override
    public Future<Coordinate> getPointElevation(Coordinate point) {
        return null;
    }

    @Override
    public Future<Path> getPathElevation(Coordinate start, Coordinate destination, Integer samples) {
        return null;
    }
}
