package io.magpi.api.handler;

import io.magpi.geo.Coordinate;
import io.magpi.maps.MapProvider;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class PointElevationHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {

        Double latitude=Double.parseDouble(event.request().params().get("latitude"));
        Double longitude=Double.parseDouble(event.request().params().get("longitude"));

        MapProvider maps=MapProvider.provider();
        Coordinate point=new Coordinate(latitude,longitude);
        Future<Coordinate> coordinateFuture= maps.getPointElevation(point);

        coordinateFuture.setHandler(res->{

            if(res.succeeded()){

                Coordinate coordinate=res.result();


                event.response().end(coordinate.json().encodePrettily());
            }else{
                JsonObject error=new JsonObject();

                error.put("error",res.cause().getMessage());

                event.response().setStatusCode(500).end(error.encodePrettily());
            }
        });


    }
}
