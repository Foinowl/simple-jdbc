package org.example.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.db.mappers.RowMapper;

public class TemplateExecutor<T> {

    protected final RowMapper<T> rowMapper;

    protected TemplateExecutor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    protected List<T> select(String sql,
                          Object... parameters) {
        try (PreparedStatement ps = createStatement(sql, parameters); ResultSet rs = ps.executeQuery()) {
            return handleResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected Optional<T> selectForEntity(String sql, Object... parameters) {
        List<T> items = select(sql, parameters);
        if (items.isEmpty() & !(items.size() == 1)) {
            return Optional.empty();
        }

        return Optional.of(items.get(0));
    }


    protected long executeInsertQuery(String query, Object... params) {
        long result = 0;
        try (PreparedStatement statement = createStatement(query, params)) {
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                result = generatedKey.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }


    protected long insert(String sql, Object... parameters) {
        try (PreparedStatement ps = createStatement(sql, parameters)) {
            ps.executeUpdate();
            ResultSet keyRS = ps.getGeneratedKeys();
            if (keyRS.next()) {
                return keyRS.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return 0;
    }

    protected long update(String sql, Object... parameters) {
        try (PreparedStatement ps = createStatement(sql, parameters)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private PreparedStatement createStatement(String sql, Object... parameters) {

        try {
            //        get from pool connections
            Connection connection = null;

            PreparedStatement ps = null;
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
//            remove used connection from pool
            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private List<T> handleResultSet(ResultSet rs) {
        List<T> entities = new ArrayList<>();
        try {
            while (rs.next()) {
                T entity = rowMapper.mapRow(rs);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return entities;
    }
}
