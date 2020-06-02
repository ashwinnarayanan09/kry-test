package se.kry.codetest.migrate;

import io.vertx.core.Vertx;
import se.kry.codetest.DBConnector;

public class DBMigration {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DBConnector connector = new DBConnector(vertx);

   /* connector.query("DROP TABLE service").setHandler(done -> {
      if(done.succeeded()){
        System.out.println("TABLE DROPPED");
      } else {
        done.cause().printStackTrace();
      }
      vertx.close(shutdown -> {
        System.exit(0);
      });
    });*/


    connector.query("CREATE TABLE IF NOT EXISTS service (name VARCHAR(50),hostname VARCHAR(50),port INT,url VARCHAR(128),status VARCHAR(10),created VARCHAR(10))").setHandler(done -> {
      if(done.succeeded()){
        System.out.println("CREATED TABLE ");
      } else {
        done.cause().printStackTrace();
      }
      vertx.close(shutdown -> {
        System.exit(0);
      });
    });


    connector.query("INSERT INTO SERVICE VALUES('kry Service','localhost',8083,'/','UNKNOWN','2020-06-02')").setHandler(done -> {
      if(done.succeeded()){
        System.out.println("INSERTED TEST DATA");
      } else {
        done.cause().printStackTrace();
      }
      vertx.close(shutdown -> {
        System.exit(0);
      });
    });
  }
}
