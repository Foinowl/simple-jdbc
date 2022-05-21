package org.example.db.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface PoolService {
    Connection getConnection();

    void releaseConnection(Connection connection);

    void destroy();
}
