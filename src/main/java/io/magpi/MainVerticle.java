package io.magpi;

import io.magpi.api.RestApi;
import io.magpi.web.handler.ThymeleafTemplateHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import static io.vertx.core.http.HttpMethod.GET;


public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void start() throws Exception {


        Router router = Router.router(vertx);
        router.exceptionHandler(e -> {
            logger.error(e.getMessage(), e);
        });


        router.mountSubRouter("/api/v1", RestApi.createRouter(vertx));

        Router staticRouter = Router.router(vertx);
        staticRouter.route().handler(StaticHandler.create());


        router.mountSubRouter("/static", staticRouter);

        TemplateEngine thymeleafEngine = ThymeleafTemplateEngine.create();
        TemplateHandler templateHandler = ThymeleafTemplateHandler
                .create(thymeleafEngine);

        Router webapp = Router.router(vertx);
        webapp.route().handler(templateHandler);

        router.mountSubRouter("/webapp",webapp);

        HttpServer http = vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);




    }
}
