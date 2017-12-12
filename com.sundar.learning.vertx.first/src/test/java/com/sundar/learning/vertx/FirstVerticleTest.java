package com.sundar.learning.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class FirstVerticleTest {

  private Vertx vertx;

  @Before
  public void setup(TestContext context) {
    int port = 8081;
    DeploymentOptions options = new DeploymentOptions().setConfig(
      new JsonObject().put("http.port", port));
    vertx = Vertx.vertx();
    vertx.deployVerticle(FirstVerticle.class.getName(), options,
      context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testFirstVerticle(TestContext context) {
    final Async async  = context.async();
    vertx.createHttpClient().getNow(8081, "localhost", "/",
      response -> {
      response.handler(body -> {
        context.assertEquals(body.toString(), "Well, I am Web Verticle");
        async.complete();
      });
    });
  }
}
