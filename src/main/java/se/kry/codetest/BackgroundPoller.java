package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import se.kry.codetest.model.Service;
import sun.security.provider.certpath.Vertex;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BackgroundPoller {

    private final WebClient client;
    private final DBConnector connector;

  public BackgroundPoller(Vertx vertx,DBConnector connector){

    client = WebClient.create(vertx);
    this.connector = connector;

  }

  public Future<List<String>> pollServices() {
    //Get Services to Poll
      connector.getServices("SELECT * FROM SERVICE").setHandler(done -> {
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
                                      System.out.println("Received response with status code" + response.statusCode());
                                      connector.updateService("UPDATE SERVICE SET STATUS = 'SUCCESS'");
                                  } else {
                                      System.out.println("Something went wrong " + ar.cause().getMessage());
                                      connector.updateService("UPDATE SERVICE SET STATUS = 'FAIL'");
                                  }
                              });

                  });

              }
          } else {
              done.cause().printStackTrace();
          }

      });

    // Send a GET request
   /* client
            .get(8083, "localhost", "/")
            .send(ar -> {
              if (ar.succeeded()) {
                // Obtain response
                HttpResponse<Buffer> response = ar.result();
                System.out.println("Received response with status code" + response.statusCode());
                connector.updateService("UPDATE SERVICE SET STATUS = 'SUCCESS'");
              } else {
                System.out.println("Something went wrong " + ar.cause().getMessage());
                  connector.updateService("UPDATE SERVICE SET STATUS = 'FAIL'");
              }
            });
*/


    return Future.failedFuture("TODO");
  }
}
