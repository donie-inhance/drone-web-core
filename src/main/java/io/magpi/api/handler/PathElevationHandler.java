package io.magpi.api.handler;

import io.magpi.geo.Coordinate;
import io.magpi.geo.GeoMath;
import io.magpi.geo.Path;
import io.magpi.maps.MapProvider;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class PathElevationHandler implements Handler<RoutingContext> {


    @Override
    public void handle(RoutingContext event) {

        Double latitude=Double.parseDouble(event.request().params().get("latitude"));
        Double longitude=Double.parseDouble(event.request().params().get("longitude"));
        Coordinate start=new Coordinate(latitude,longitude);

        Coordinate destination;

        if(event.request().params().contains("destinationLatitude")){

            double destinationLatitude = Double.parseDouble(event.request().params().get("destinationLatitude"));
            double destinationLongitude = Double.parseDouble(event.request().params().get("destinationLongitude"));
            destination=new Coordinate(destinationLatitude,destinationLongitude);
        }else{

            Double bearing=0d;
            if(event.request().params().contains("bearing")) {
               bearing= Double.parseDouble(event.request().params().get("bearing"));
            }
            Integer distance=1000;
            if(event.request().params().contains("length")) {
              distance= Integer.parseInt(event.request().params().get("length"));
            }

             destination= GeoMath.destinationPoint(start,bearing,distance.doubleValue());

        }

        Integer samples=10;
        if(event.request().params().contains("samples")) {
            samples=Integer.parseInt(event.request().params().get("samples"));
        }

        MapProvider maps=MapProvider.provider();

        Future<Path> pathFuture=maps.getPathElevation(start,destination,samples);

        pathFuture.setHandler(res->{

            if(res.succeeded()){

                Path path=res.result();


                event.response().end(path.json().encodePrettily());
            }else{
                JsonObject error=new JsonObject();

                error.put("error",res.cause().getMessage());

                event.response().setStatusCode(500).end(error.encodePrettily());
            }

        });

    }
}
