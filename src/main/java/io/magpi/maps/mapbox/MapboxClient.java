package io.magpi.maps.mapbox;

import io.magpi.maps.MapProvider;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;

public class MapboxClient implements MapProvider
{

    private static final String HOST ="https://api.mapbox.com";
    private static final String MAP = "";
    private static final String ACCESS_TOKEN="pk.eyJ1IjoicGF0cmljazExY29ud2F5IiwiYSI6ImNpdzR3djBiYjAwMHgyb3BjMHc0Y3p3YjAifQ.0MzVfaGqqRYaYUqqQRAvQg";




    public Future<Float> getElevation(Float latitude, Float longitude) {

        Future<Float> result=Future.future();
        MapProvider.createHttpClient(ar->{

            HttpClient client = ar.result();

            String path="/v4/surface/mapbox.mapbox-terrain-v2.json?" +
                    "access_token="+ACCESS_TOKEN+
                    "&layer=contour&fields=ele" +
                    "&points="+latitude+","+longitude;

        });

        return result;
    }
}
