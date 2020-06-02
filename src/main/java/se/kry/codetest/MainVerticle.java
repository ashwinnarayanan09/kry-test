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

  //private HashMap<String, String> services = new HashMap<>();
  private List<Service> serviceList;
  //TODO use this
  private DBConnector connector;
  private BackgroundPoller poller ;

  @Override
  public void start(Future<Void> startFuture) {

    connector = new DBConnector(vertx);
    poller = new BackgroundPoller(vertx,connector);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    //services.put("https://www.kry.se", "UNKNOWN");
    vertx.setPeriodic(1 * 1, timerId -> poller.pollServices());

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
    router.get("/kry/api/services").handler(this::getServices);
    router.post("/kry/api/services/add").handler(this::addService);
    router.post("/kry/api/services/delete").handler(this::addService);

    router.post("/service_old").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
     // services.put(jsonBody.getString("url"), "UNKNOWN");
      connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
      req.response()
          .putHeader("content-type", "text/plain")
          .end("OK");
    });
  }

  private void getServices(RoutingContext routingContext) {

    connector.getServices("SELECT * FROM SERVICE").setHandler(done -> {
      List<JsonObject> rows = null;
      if(done.succeeded()){
        System.out.println("Data Selected");
        ResultSet rs = done.result();
        for (JsonArray line : rs.getResults()) {
          System.out.println(line.encode());
        }
        if(rs.getNumRows() > 0){
          rows = rs.getRows();
        }

        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(rows.toString());

      } else {
        done.cause().printStackTrace();
      }

    });;
  }

  private void addService(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    //services.put(jsonBody.getString("url"), "UNKNOWN");
    String sql = "INSERT INTO SERVICE VALUES('TEST SERVICE','"+jsonBody.getString("url")+"','UNKNOWN')";
    //connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
    connector.saveService(sql);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }

  private void deleteService(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    //services.put(jsonBody.getString("url"), "UNKNOWN");
    String sql = "DELETE FROM SERVICE WHERE NAME = 'KRY'";
    //connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
    connector.saveService(sql);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }


  private void updateServiceStatus(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    //services.put(jsonBody.getString("url"), "UNKNOWN");
    String sql = "UPDATE SERVICE SET STATUS = 'OK'";
    //connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
    connector.saveService(sql);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }

}



