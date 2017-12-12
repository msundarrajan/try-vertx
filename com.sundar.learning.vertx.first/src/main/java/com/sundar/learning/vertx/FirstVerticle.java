package com.sundar.learning.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirstVerticle extends AbstractVerticle {

  private Map<Integer, Person> products = new LinkedHashMap<>();
  private void createSomeData() {
    Person bowmore = new Person("Bowmore", "Scotland, Islay");
    products.put(bowmore.getId(), bowmore);
    Person talisker = new Person("Talisker", "Scotland, Island");
    products.put(talisker.getId(), talisker);
  }

  private void addOne(RoutingContext routingContext) {
    final Person whisky = Json.decodeValue(routingContext.getBodyAsString(),
      Person.class);
    products.put(whisky.getId(), whisky);
    routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(whisky));
  }

  private void deleteOne(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Integer idAsInteger = Integer.valueOf(id);
      products.remove(idAsInteger);
    }
    routingContext.response().setStatusCode(204).end();
  }

  private void getAll(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(products.values()));
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    createSomeData();
    Router router = Router.router(vertx);

    router.route("/").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response
        .putHeader("content-type", "text/html")
        .end("Well, I am Web Verticle");
    });

    router.route("/assets/*").handler(StaticHandler.create("assets"));

    router.get("/api/whiskies").handler(this::getAll);
    router.route("/api/whiskies*").handler(BodyHandler.create());
    router.post("/api/whiskies").handler(this::addOne);
    router.delete("/api/whiskies/:id").handler(this::deleteOne);

    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(config().getInteger("http.port", 8080), result -> {
        if (result.succeeded()) {
          startFuture.complete();
        } else {
          startFuture.fail(result.cause());
        }
      });
  }
}
