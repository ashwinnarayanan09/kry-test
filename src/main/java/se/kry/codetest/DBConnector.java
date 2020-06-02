package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class DBConnector {

  private final String DB_PATH = "poller.db";
  private final SQLClient client;

  public DBConnector(Vertx vertx){
    JsonObject config = new JsonObject()
        .put("url", "jdbc:sqlite:" + DB_PATH)
        .put("driver_class", "org.sqlite.JDBC")
        .put("max_pool_size", 30);

    client = JDBCClient.createShared(vertx, config);

  }

  public Future<ResultSet> query(String query) {

    return query(query, new JsonArray());
  }


  public Future<ResultSet> query(String query, JsonArray params) {
    if(query == null || query.isEmpty()) {
      return Future.failedFuture("Query is null or empty");
    }
    if(!query.endsWith(";")) {
      query = query + ";";
    }

    Future<ResultSet> queryResultFuture = Future.future();

    client.queryWithParams(query, params, result -> {

      System.out.println(result.toString());
      if(result.failed()){
        queryResultFuture.fail(result.cause());
      }
      else if(result.succeeded()){
        queryResultFuture.complete(result.result());
      }
      else {
        queryResultFuture.complete(result.result());
      }
    });

    return queryResultFuture;
  }


  public Future<ResultSet> execute(String query,JsonArray params) {
    Future<ResultSet> queryResultFuture = Future.future();
    client.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();
        connection.queryWithParams(query,params, result -> {
          if (result.succeeded()) {
            queryResultFuture.complete(result.result());
          }
        });
      } else {
        // Failed to get connection - deal with it
      }
    });
    return queryResultFuture;
  }


  public Future<ResultSet> updateService(String query) {
    Future<ResultSet> queryResultFuture = Future.future();
    client.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();
        connection.query(query, result -> {
          if (result.succeeded()) {
            queryResultFuture.complete(result.result());
          }
        });
      } else {
        // Failed to get connection - deal with it
      }
    });
    return queryResultFuture;
  }



}
