package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.model.Employee;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(final ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong("empl_id"));
        employee.setName(resultSet.getString("empl_title"));
        return employee;
    }
}
