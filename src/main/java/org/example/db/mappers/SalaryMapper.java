package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.model.Salary;

public class SalaryMapper implements RowMapper<Salary> {
    @Override
    public Salary mapRow(ResultSet resultSet) throws SQLException {
        Salary salary = new Salary();
        salary.setId(resultSet.getLong("salary_id"));
        salary.setValue(resultSet.getBigDecimal("salary_value"));
        return salary;
    }
}
