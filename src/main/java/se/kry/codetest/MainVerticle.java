package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.codetest.model.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private HashMap<String, String> services = new HashMap<>();
  //TODO use this
  private DBConnector connector;
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    //services.put("https://www.kry.se", "UNKNOWN");
    vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(services));

    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8083, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/*").handler(StaticHandler.create());
    router.get("/service").handler(req -> {
      connector.getServices("SELECT * FROM SERVICE").setHandler(done -> {
        if(done.succeeded()){
          System.out.println("Data Selected");
          ResultSet rs = done.result();
          for (JsonArray line : rs.getResults()) {
            System.out.println(line.encode());
          }

          req.response()
                  .putHeader("content-type", "application/json")
                  .end(new JsonArray(rs.getResults()).encode());

        } else {
          done.cause().printStackTrace();
        }

      });;

    });

    router.post("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      services.put(jsonBody.getString("url"), "UNKNOWN");
      connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
      req.response()
          .putHeader("content-type", "text/plain")
          .end("OK");
    });
  }

  private void getArticles(RoutingContext routingContext) {
    String articleId = routingContext.request()
            .getParam("id");
    Service service = new Service();

    routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(200)
            .end(Json.encodePrettily(service));
  }

}



