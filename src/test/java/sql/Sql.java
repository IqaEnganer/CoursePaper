package sql;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;


public class Sql {
    public Sql() {
    }

    static final String url = "jdbc:mysql://localhost:3306/app";
    static final String user = "pass";
    static final String pass = "dav";

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class StatusResponse {
        private String status;
    }

    // Чек кол-ва записей покупок
    @SneakyThrows
    public static String checkStatus() {
        var runner = new QueryRunner();
        var status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            var statusCheck = runner.query(conn, status, new BeanHandler<>(StatusResponse.class));
            return statusCheck.getStatus();
        }
    }

    // Чек кол-ва записей покупок в кредит
    @SneakyThrows
    public static String checkStatusCredit() {
        var runner = new QueryRunner();
        var status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            var statusCheck = runner.query(conn, status, new BeanHandler<>(StatusResponse.class));
            return statusCheck.getStatus();
        }
    }

    // Чек общего кол-ва записей в бд
    @SneakyThrows
    public static long getNumberOfRawsFromOrderEntity() {
        QueryRunner runner = new QueryRunner();
        String dataSQL = "SELECT COUNT(id) FROM order_entity";
        long numberOfRaws;
        try (
                Connection connection = DriverManager.getConnection(
                        url, user, pass
                )
        ) {
            numberOfRaws = runner.query(connection, dataSQL, new ScalarHandler<>());
        }
        return numberOfRaws;
    }
}


