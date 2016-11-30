package io.magpi.maps.google_maps;

import io.magpi.maps.MapProvider;
import io.magpi.maps.MapProviderException;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.vertx.core.http.HttpHeaders.ACCEPT;

public class GoogleMapsClient implements MapProvider {

    private static final String HOST ="maps.googleapis.com";
    private static final String ACCESS_TOKEN="AIzaSyBU554aUTSBkQo7ShLY9znCJ2_18zZxuHw";
    private static final int PORT = 443;

    @Override
    public Future<Float> getElevation(Float latitude, Float longitude) {

        //https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034&key=YOUR_API_KEY

        /*
        {
           "results" : [
              {
                 "elevation" : 1608.637939453125,
                 "location" : {
                    "lat" : 39.73915360,
                    "lng" : -104.98470340
                 },
                 "resolution" : 4.771975994110107
              }
           ],
           "status" : "OK"
        }
         */
        Future<Float> result=Future.future();
        MapProvider.createHttpClient(ar->{

            HttpClient client = ar.result();

            String path="/maps/api/elevation/json?locations=LAT,LONG&key=API_KEY"
                    .replace("LAT",latitude.toString())
                    .replace("LONG",longitude.toString())
                    .replace("API_KEY",ACCESS_TOKEN);

            System.out.println(path);

            HttpClientRequest request = client.get(PORT,HOST, path);

            request.headers().add(ACCEPT,APPLICATION_JSON);

            request.handler(response->{


                if(response.statusCode()==200) {
                    response.bodyHandler(body -> {

                        JsonObject json = body.toJsonObject();

                        JsonArray results = json.getJsonArray("results");
                        JsonObject firstResult = results.getJsonObject(0);
                        if(firstResult==null||!firstResult.containsKey("elevation")){
                            result.fail(new MapProviderException("No elevation in google response"));
                        }
                        Float elevation = firstResult.getFloat("elevation");
                        result.complete(elevation);
                    });
                }else{
                    result.fail(new MapProviderException("Google request failed with status "+response.statusMessage()));
                }



            });

            request.end();

        });

        return result;
    }
}
