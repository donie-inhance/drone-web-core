package io.magpi.api;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class RestApi  {


    public static Router createRouter(Vertx vertx) {

        Router apiRouter=Router.router(vertx);



        return apiRouter;
    }
}
