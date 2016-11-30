package io.magpi.api;

import io.magpi.api.handler.PointElevationHandler;
import io.magpi.api.handler.GridElevationHandler;
import io.magpi.api.handler.PathElevationHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import static io.vertx.core.http.HttpMethod.GET;

public class RestApi  {


    public static Router createRouter(Vertx vertx) {

        Router apiRouter=Router.router(vertx);

        /*
            Given lat , long
            and return elevation in meters

            method :GET
            params:
                    latitude ........ gps latitude

                    longitude ....... gps longitude


         */
        apiRouter.route(GET,"/elevation/point").handler(new PointElevationHandler());


        /*
            Given lat , long and bearing and distance
            and return an array of gps coordinates and with elevations

            method :GET
            params:
                    latitude ........ gps latitude  start of path

                    longitude ....... gps longitude  start of path

                    bearing  ........ bearing in degrees ........ optional default 0

                    length ......... length of path in meters ... optional default 1000

                    samples ......... how many points on path ... optional default 20



         */
        apiRouter.route(GET,"/elevation/path").handler(new PathElevationHandler());

        /*
            Given lat , long and bearing calculate a grid
            and return grid as an array of gps coordinates and with elevations

            method :GET
            params:
                    latitude ........ gps latitude  grid center

                    longitude ....... gps longitude  grid center

                    bearing  ........ bearing in degrees ......... optional default 0

                    length .......... length of grid in meters ... optional default 1000

                    samples ......... how many points on a row ... optional default 10 ...max 14



         */
        apiRouter.route(GET,"/elevation/grid").handler(new GridElevationHandler());



        return apiRouter;
    }
}
