package org.example.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Можно сделать класс абстрактным и классы использующие соединения через jdbc могут от него наследоваться.
public class JdbcTemplate {
    public static <T, Y> T select(Connection c, String sql, ResultSetHandler<T, Y> resultSetHandler, Callback<Y> cb,
                                  Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement(ps, parameters);
            try (ResultSet rs = ps.executeQuery()) {
                return resultSetHandler.handle(rs, cb);
            }
        }
    }

    public static <T, Y> T insert(Connection c, String sql, ResultSetHandler<T, Y> resultSetHandler, Callback<Y> cb,
                                  Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement(ps, parameters);
            int result = ps.executeUpdate();
            if (result != 1) {
                throw new SQLException("Не получается добавить запись - " + result);
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return resultSetHandler.handle(rs, cb);
            }
        }
    }

    public static int update(Connection c, String sql, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            preparedStatement(ps, parameters);
            return ps.executeUpdate();
        }
    }

    private static void preparedStatement(PreparedStatement ps, Object... parameters)
        throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
        }
    }
}
