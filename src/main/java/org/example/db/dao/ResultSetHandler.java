package org.example.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T, Y> {
    T handle(ResultSet resultSet, Callback<Y> callback) throws SQLException;
}
