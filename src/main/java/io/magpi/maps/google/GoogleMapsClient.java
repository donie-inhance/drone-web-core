package io.magpi.maps.google;

import io.magpi.geo.Coordinate;
import io.magpi.geo.GeoMath;
import io.magpi.geo.Grid;
import io.magpi.geo.Path;
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
    public Future<Coordinate> getPointElevation(Coordinate point) {

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
        Future<Coordinate> result=Future.future();
        MapProvider.createHttpClient(ar->{

            HttpClient client = ar.result();

            String path="/maps/api/elevation/json?locations=LATLONG&key=API_KEY"
                    .replace("LATLONG",point.latLongStr())
                    .replace("API_KEY",ACCESS_TOKEN);


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
                        }else {
                            Double elevation = firstResult.getDouble("elevation");

                            point.setElevation(elevation);
                        }
                        if(firstResult.containsKey("resolution")){
                            point.setResolution(firstResult.getDouble("resolution"));
                        }
                        result.complete(point);
                    });
                }else{
                    result.fail(new MapProviderException("Google request failed with status "+response.statusMessage()));
                }



            });

            request.end();

        });

        return result;
    }

    @Override
    public Future<Path> getPathElevation(Coordinate start, Coordinate destination, Integer samples) {

        Future<Path> result=Future.future();

        MapProvider.createHttpClient(ar->{
            HttpClient client = ar.result();


            String path="/maps/api/elevation/json?path=PATH&samples=SAMPLES&key=API_KEY"
                    .replace("PATH",start.latLongStr()+"|"+destination.latLongStr())
                    .replace("SAMPLES",samples.toString())
                    .replace("API_KEY",ACCESS_TOKEN);

            HttpClientRequest request = client.get(PORT,HOST, path);

            request.headers().add(ACCEPT,APPLICATION_JSON);

            request.handler(response->{


                if(response.statusCode()==200) {
                    response.bodyHandler(body -> {

                        JsonObject json = body.toJsonObject();

                        JsonArray results = json.getJsonArray("results");

                        Path thePath=new Path();

                        for(int i=0;i<results.size();i++){

                            JsonObject item=results.getJsonObject(i);
                            Double elevation=item.getDouble("elevation");
                            Double lat=item.getJsonObject("location").getDouble("lat");
                            Double lng=item.getJsonObject("location").getDouble("lng");
                            Coordinate coord=new Coordinate(lat,lng,elevation);

                            if(item.containsKey("resolution")){
                                Double resolution=item.getDouble("resolution");
                                coord.setResolution(resolution);

                            }
                            thePath.add(coord);

                        }
                        result.complete(thePath);

                    });
                }else{
                    result.fail(new MapProviderException("Google request failed with status "+response.statusMessage()));
                }



            });

            request.end();

        });

        return result;
    }

    @Override
    public Future<Grid> getGridElevation(Coordinate center, Double bearing, Double distance, Integer samples) {

        Grid grid= GeoMath.grid(center,bearing,distance,samples);


        Future<Grid> result=Future.future();
        MapProvider.createHttpClient(ar->{

            HttpClient client = ar.result();

            String path="/maps/api/elevation/json?locations=LOCATIONS&key=API_KEY"
                    .replace("LOCATIONS",grid.latLongStr())
                    .replace("API_KEY",ACCESS_TOKEN);


            HttpClientRequest request = client.get(PORT,HOST, path);

            request.headers().add(ACCEPT,APPLICATION_JSON);

            request.handler(response->{


                if(response.statusCode()==200) {
                    response.bodyHandler(body -> {

                        JsonObject json = body.toJsonObject();

                        JsonArray results = json.getJsonArray("results");

                        Grid theGrid=new Grid();
                        Path row=new Path();
                        for(int i=0;i<results.size();i++){

                            JsonObject item=results.getJsonObject(i);
                            Double elevation=item.getDouble("elevation");
                            Double lat=item.getJsonObject("location").getDouble("lat");
                            Double lng=item.getJsonObject("location").getDouble("lng");
                            Coordinate coord=new Coordinate(lat,lng,elevation);

                            if(item.containsKey("resolution")){
                                Double resolution=item.getDouble("resolution");
                                coord.setResolution(resolution);

                            }
                            row.add(coord);

                            if(i%(samples-1)==0){
                                theGrid.add(row);
                                row=new Path();
                            }
                        }

                        result.complete(theGrid);


                    });
                }else{
                    response.bodyHandler(body->{
                       System.out.println(body.toString());
                    });
                    result.fail(new MapProviderException("Google request failed with status "+response.statusMessage()));
                }



            });

            request.end();

        });

        return result;
    }
}
