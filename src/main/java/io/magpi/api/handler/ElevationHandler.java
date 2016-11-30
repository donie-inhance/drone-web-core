package io.magpi.api.handler;

import io.magpi.maps.MapProvider;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ElevationHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {

        Float latitude=Float.parseFloat(event.request().params().get("latitude"));
        Float longitude=Float.parseFloat(event.request().params().get("longitude"));

        MapProvider maps=MapProvider.provider();

        Future<Float> elevation= maps.getElevation(latitude,longitude);

        elevation.setHandler(res->{

            if(res.succeeded()){
                JsonObject result=new JsonObject();
                result.put("elevation",res.result());
                event.response().end(result.encodePrettily());
            }else{
                JsonObject error=new JsonObject();

                error.put("error",res.cause().getMessage());

                event.response().setStatusCode(500).end(error.encodePrettily());
            }
        });


    }
}
