package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.entity.SalaryEntity;

public class SalaryEntityMapper implements RowMapper<SalaryEntity>{
    @Override
    public SalaryEntity mapRow(ResultSet resultSet) throws SQLException {
        SalaryEntity entity = new SalaryEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setValue(resultSet.getBigDecimal("value"));
        return entity;
    }
}
