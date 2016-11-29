package io.magpi;

import io.magpi.api.RestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void start() throws Exception {


        Router router = Router.router(vertx);
        router.exceptionHandler(e -> {
            logger.error(e.getMessage(), e);
        });


        router.mountSubRouter("/api/v1", RestApi.createRouter(vertx));


    }
}
