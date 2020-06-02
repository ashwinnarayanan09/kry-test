package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import se.kry.codetest.model.Service;
import java.util.List;


public class MainVerticle extends AbstractVerticle {

  //private HashMap<String, String> services = new HashMap<>();
  private List<Service> serviceList;
  //TODO use this
  private DBConnector connector;
  private BackgroundPoller poller ;
  private final String SELECT_QUERY = "SELECT * FROM SERVICE";
  private final String UPDATE_QUERY = "UPDATE SERVICE SET HOSTNAME = ?,PORT = ?, URL = ? WHERE NAME = ?";
  private final String DELETE_QUERY = "DELETE FROM SERVICE WHERE NAME = ?";
  private final String INSERT_QUERY = "INSERT INTO SERVICE VALUES (?,?,?,?,?,?)";


  @Override
  public void start(Future<Void> startFuture) {

    connector = new DBConnector(vertx);
    poller = new BackgroundPoller(vertx,connector);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    //services.put("https://www.kry.se", "UNKNOWN");
  //  vertx.setPeriodic(1000 * 30, timerId -> poller.pollServices());

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
    router.put("/kry/api/services/update").handler(this::updateService);
    router.delete("/kry/api/services/delete").handler(this::deleteService);

  }

  private void getServices(RoutingContext routingContext) {

    JsonArray params = new JsonArray();
    connector.execute(SELECT_QUERY,params).setHandler(done -> {
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

    });
  }

  private void addService(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    JsonArray params = new JsonArray()
            .add(jsonBody.getString("name"))
            .add(jsonBody.getString("hostname"))
            .add(jsonBody.getInteger("port"))
            .add(jsonBody.getString("url"))
            .add("2020")
            .add("UNKNOWN")
            ;
    connector.execute(INSERT_QUERY,params);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }

  private void updateService(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    JsonArray params = new JsonArray()
            .add(jsonBody.getString("hostname"))
            .add(jsonBody.getInteger("port"))
            .add(jsonBody.getString("url"))
            .add(jsonBody.getString("name"))
            ;
    connector.execute(UPDATE_QUERY,params);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }

  private void deleteService(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    JsonArray params = new JsonArray().add(jsonBody.getString("name"));
    connector.execute(DELETE_QUERY,params);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }


  private void updateServiceStatus(RoutingContext routingContext){

    JsonObject jsonBody = routingContext.getBodyAsJson();
    //services.put(jsonBody.getString("url"), "UNKNOWN");
    String sql = "UPDATE SERVICE SET STATUS = 'OK'";
    //connector.saveService("INSERT INTO SERVICE VALUES('TEST SERVICE','TEST URL','UNKNOWN')");
    JsonArray params = new JsonArray();
    connector.execute(UPDATE_QUERY,params);
    routingContext.response()
            .putHeader("content-type", "text/plain")
            .end("OK");
  }

}



