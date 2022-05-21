package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.entity.EmployeeEntity;

public class EmployeeEntityMapper implements RowMapper<EmployeeEntity>{
    @Override
    public EmployeeEntity mapRow(ResultSet resultSet) throws SQLException {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("ename"));
        entity.setSalaryId(resultSet.getLong("salary_id"));
        entity.setOrgId(resultSet.getLong("org_id"));
        return entity;
    }
}
