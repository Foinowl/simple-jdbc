package org.example.db.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionConnection {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";

    private final String user = "postgres";
    private final String password = "postgres";


    public TransactionConnection() {

    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void destroy(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
