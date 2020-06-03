package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.util.List;


public class BackgroundPoller {

    private final String SELECT_QUERY = "SELECT * FROM SERVICE";
    private final String UPDATE_SUCCESS_QUERY = "UPDATE SERVICE SET STATUS = 'OK' WHERE NAME = ?";
    private final String UPDATE_ERROR_QUERY = "UPDATE SERVICE SET STATUS = 'FAIL' WHERE NAME = ?";
    private final WebClient client;
    private final DBConnector connector;

  public BackgroundPoller(Vertx vertx,DBConnector connector){

    client = WebClient.create(vertx);
    this.connector = connector;

  }

  public Future<List<String>> pollServices() {
    //Get Services to Poll
      JsonArray params = new JsonArray();
      connector.execute(SELECT_QUERY,params).setHandler(done -> {
          List<JsonObject> rows = null;
          if(done.succeeded()){
              ResultSet rs = done.result();
              if(rs.getNumRows() > 0){
                  rows = rs.getRows();
                  rows.forEach(row -> {

                      // Send a GET request
                      client
                              .get(row.getInteger("port"), row.getString("hostname"), row.getString("url"))
                              .send(ar -> {
                                  if (ar.succeeded()) {
                                      // Obtain response
                                      HttpResponse<Buffer> response = ar.result();
                                      JsonArray successServiceParams = new JsonArray().add(row.getString("name"));
                                      System.out.println("Received response with status code" + response.statusCode());
                                      connector.execute(UPDATE_SUCCESS_QUERY,successServiceParams);
                                  } else {
                                      JsonArray errorServiceParams = new JsonArray().add(row.getString("name"));
                                      System.out.println("Something went wrong " + ar.cause().getMessage());
                                      connector.execute(UPDATE_ERROR_QUERY,errorServiceParams);
                                  }
                              });

                  });

              }
          } else {
              done.cause().printStackTrace();
          }

      });

    return Future.failedFuture("TODO");
  }
}
