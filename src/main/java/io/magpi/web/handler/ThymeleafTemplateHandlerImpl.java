package io.magpi.web.handler;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.impl.Utils;
import io.vertx.ext.web.templ.TemplateEngine;

public class ThymeleafTemplateHandlerImpl implements TemplateHandler {
    private final TemplateEngine engine;
    private final String templateDirectory;
    private final String contentType;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public ThymeleafTemplateHandlerImpl(TemplateEngine engine, String templateDirectory, String contentType) {
        this.engine = engine;
        this.templateDirectory = templateDirectory;
        this.contentType = contentType;
    }

    @Override
    public void handle(RoutingContext context) {
        String file = templateDirectory + Utils.pathOffset(context.normalisedPath(), context);
        if (!file.endsWith(".html")) {
            file += ".html";
        }
        engine.render(context, file, (res) -> {
            if (res.succeeded()) {
                context.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType).end(res.result());
            } else {
                context.fail(res.cause());
            }

        });
    }
}
