package io.magpi.maps;

import io.magpi.geo.Coordinate;
import io.magpi.geo.Grid;
import io.magpi.geo.Path;
import io.magpi.maps.google.GoogleMapsClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public interface MapProvider{

    int TIMEOUT = 30;

    static void createHttpClient(Handler<AsyncResult<HttpClient>> handler) {
        Vertx.currentContext().executeBlocking(f -> {
            HttpClient client = Vertx.currentContext().owner().createHttpClient(new HttpClientOptions()
                    .setIdleTimeout(TIMEOUT)
                    .setConnectTimeout(TIMEOUT * 1000)
                    .setSsl(true));
            f.complete(client);
        }, handler);
    }

    static MapProvider provider() {

        return new GoogleMapsClient();
        //return new MapboxClient();
    }


    Future<Coordinate> getPointElevation(Coordinate point);

    Future<Path> getPathElevation(Coordinate start, Coordinate destination, Integer samples);

    Future<Grid> getGridElevation(Coordinate center, Double bearing, Double v, Integer samples);
}
