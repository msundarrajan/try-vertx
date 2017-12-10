package com.sundar.learning.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class FirstVerticle extends AbstractVerticle {
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx
      .createHttpServer()
      .requestHandler(r -> {
        r.response().end("Well, I am Verticle");
      })
      .listen(8080, result -> {
        if (result.succeeded()) {
          startFuture.complete();
        } else {
          startFuture.fail(result.cause());
        }
      });
  }
}

