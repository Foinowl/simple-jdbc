package org.example.db.dao.console;

import java.util.HashSet;
import java.util.Set;
import org.example.db.dao.ResultSetHandler;
import org.example.db.mappers.console.EmployeeMapper;
import org.example.db.mappers.console.OrganizationMapper;
import org.example.db.mappers.RowMapper;
import org.example.db.mappers.console.SalaryMapper;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.model.Salary;

public class UtilsHandler {
    private static final RowMapper<Salary> salaryRowMapper = new SalaryMapper();

    protected static final ResultSetHandler<Salary, Salary> ONE_SALARY_HANDLER = (rs, cb) -> {
        if (rs.next()) {
            return salaryRowMapper.mapRow(rs);
        } else {
            return null;
        }
    };

    private static final RowMapper<Employee> employeeRowMapper = new EmployeeMapper();

    private static final RowMapper<Organization> organizationRowMapper = new OrganizationMapper();

    protected static final ResultSetHandler<Employee, Employee> ONE_EMPLOYEE_HANDLER = (rs, cb) -> {
        if (rs.next()) {
            Employee employee = employeeRowMapper.mapRow(rs);
            return cb.call(rs, employee);

        } else {
            return null;
        }
    };

    protected static final ResultSetHandler<Organization, Organization> ONE_ORGANIZATION_HANDLER = (rs, cb) -> {
        if (rs.next()) {
            Organization organization = organizationRowMapper.mapRow(rs);
            return cb.call(rs, organization);

        } else {
            return null;
        }
    };

    protected static final ResultSetHandler<Set<Employee>, Employee> SET_EMPLOYEES_HANDLER = (rs, cb) -> {
        Set<Employee> list = new HashSet<>();
        while (rs.next()) {
            rs.previous();
            Employee employee = ONE_EMPLOYEE_HANDLER.handle(rs, cb);
            list.add(employee);

        }
        return list;
    };
}
