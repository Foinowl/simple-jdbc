package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.entity.EmployeeEntity;

public class EmployeeEntityMapper implements RowMapper<EmployeeEntity>{
    @Override
    public EmployeeEntity mapRow(ResultSet resultSet) throws SQLException {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));
        entity.setSalaryId(resultSet.getLong("salaryId"));
        entity.setOrgId(resultSet.getLong("orgId"));
        return entity;
    }
}
