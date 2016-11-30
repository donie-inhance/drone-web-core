package io.magpi.api.handler;

import io.magpi.geo.Coordinate;
import io.magpi.geo.Grid;
import io.magpi.maps.MapProvider;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class GridElevationHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext event) {

        Double latitude=Double.parseDouble(event.request().params().get("latitude"));
        Double longitude=Double.parseDouble(event.request().params().get("longitude"));
        Coordinate center=new Coordinate(latitude,longitude);

        Double bearing=0d;
        if(event.request().params().contains("bearing")) {
            bearing= Double.parseDouble(event.request().params().get("bearing"));
        }

        Integer distance=1000;
        if(event.request().params().contains("length")) {
            distance= Integer.parseInt(event.request().params().get("length"));
        }

        Integer samples=10;
        if(event.request().params().contains("samples")) {
            samples=Integer.parseInt(event.request().params().get("samples"));
        }



        MapProvider maps=MapProvider.provider();

        Future<Grid> gridFuture=maps.getGridElevation(center,bearing,distance.doubleValue(),samples);

        gridFuture.setHandler(res->{

            if(res.succeeded()){

                Grid grid=res.result();


                event.response().end(grid.json().encodePrettily());
            }else{
                JsonObject error=new JsonObject();

                error.put("error",res.cause().getMessage());

                event.response().setStatusCode(500).end(error.encodePrettily());
            }

        });

    }
}
