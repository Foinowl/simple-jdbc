package org.example.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback<T> {

    T call(ResultSet rs, T obj) throws SQLException;
}
