package org.example.db.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PoolConnections implements PoolService{
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;

    private final int poolSize = 10;

    private static PoolConnections INSTANCE;


    private TransactionConnection transactionConnection;
    private PoolConnections() {
    }

    public static PoolConnections getInstance() {

        if (INSTANCE == null)
        {
            synchronized (PoolConnections.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new PoolConnections();
                }
            }

        }
        return INSTANCE;
    }


    public void init(TransactionConnection transactionConnection) {
        this.transactionConnection = transactionConnection;
        availableConnections = new ArrayBlockingQueue<>(poolSize);
        usedConnections = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Connection connection = transactionConnection.getConnection();
            availableConnections.add(connection);
        }
    }

    @Override
    public Connection getConnection() {
        Connection connection;
        try {
            connection = availableConnections.take();
            usedConnections.put(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            try {
                availableConnections.put(connection);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    public void destroy() {
        try {
            for (Connection connection : availableConnections) {
                connection.close();
            }
            for (Connection connection : usedConnections) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
