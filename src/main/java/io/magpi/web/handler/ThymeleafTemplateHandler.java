package io.magpi.web.handler;

import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.TemplateEngine;

public interface ThymeleafTemplateHandler extends TemplateHandler {


    static TemplateHandler create(TemplateEngine engine) {
        return new ThymeleafTemplateHandlerImpl(engine, "webapp", "text/html");
    }


    static TemplateHandler create(TemplateEngine engine, String templateDirectory, String contentType) {
        return new ThymeleafTemplateHandlerImpl(engine, templateDirectory, contentType);
    }
}