package org.example.db.dao.console;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.dao.console.Callback;

public interface ResultSetHandler<T, Y> {
    T handle(ResultSet resultSet, Callback<Y> callback) throws SQLException;
}
