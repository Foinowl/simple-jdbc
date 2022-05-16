package org.example.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.example.db.mappers.EmployeeMapper;
import org.example.db.mappers.OrganizationMapper;
import org.example.db.mappers.RowMapper;
import org.example.db.mappers.SalaryMapper;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.model.Salary;
import org.postgresql.util.PSQLException;

public class PersistentStorage implements StorageService {

    private final static PersistentStorage instance = new PersistentStorage();

    private PersistentStorage() {
    }

    public static PersistentStorage getInstance() {
        return instance;
    }

    public void add(Organization org, Employee employee) {
        try {
            TransactionWrapper.getInstance().add(org.getTitle(), employee.getName(), employee.getSalary().getValue());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Organization findOrganization(final String title) {
        try {
            return TransactionWrapper.getInstance().findOrganization(title);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee get(final String name) {
        return TransactionWrapper.getInstance().get(name);
    }

    @Override
    public Set<Employee> listAll() {
        return TransactionWrapper.getInstance().list();
    }

    @Override
    public Employee delete(final String name) {
        return TransactionWrapper.getInstance().delete(name);
    }

    @Override
    public Employee update(final Employee newOne) {
        return TransactionWrapper.getInstance().update(newOne);
    }

    public static class TransactionWrapper {
        private static final TransactionWrapper instance = new TransactionWrapper();

        private final RowMapper<Salary> salaryRowMapper = new SalaryMapper();

        private final RowMapper<Organization> organizationRowMapper = new OrganizationMapper();

        private final RowMapper<Employee> employeeRowMapper = new EmployeeMapper();

        private Connection connection;

        private TransactionWrapper() {
            String url = "jdbc:postgresql://localhost:5433/postgres";

            String user = "postgres";
            String password = "admin";

            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static TransactionWrapper getInstance() {
            return instance;
        }

        public Set<Employee> list() {
            Set<Employee> result = new HashSet<>();

            try {
                PreparedStatement statement =
                    connection.prepareStatement(
                        "select id as empl_id, ename as empl_title, salary_id, org_id from employees");
                PreparedStatement statement1 = connection.prepareStatement(
                    "select id as salary_id, value as salary_value from salary where id = ?");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {

                    Employee employee = employeeRowMapper.mapRow(resultSet);

                    statement1.setLong(1, resultSet.getLong("salary_id"));
                    ResultSet salaryResultSet = statement1.executeQuery();

                    Salary salary = null;
                    while (salaryResultSet.next()) {
                        salary = salaryRowMapper.mapRow(salaryResultSet);
                    }
                    employee.setSalary(salary);

                    result.add(employee);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return result;
        }

        public void add(String orgTitle, String emplName, BigDecimal empSalary) throws SQLException {
            PreparedStatement addEmployee =
                connection.prepareStatement("insert into employees(ename, salary_id, org_id) values (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            Salary salary = addSalary(empSalary);

            Organization organization = addOrganization(orgTitle);
            addEmployee.setString(1, emplName);
            addEmployee.setLong(2, salary.getId());
            addEmployee.setLong(3, organization.getId());

            addEmployee.execute();
        }

        private Salary addSalary(BigDecimal salary) {
            Salary salary1 = null;
            try {
                PreparedStatement addSalary = connection.prepareStatement(
                    "insert into salary(value) values(?) returning id as salary_id, value as salary_value",
                    Statement.RETURN_GENERATED_KEYS);

                addSalary.setBigDecimal(1, salary);

                try {
                    addSalary.execute();
                    ResultSet resultSalary = addSalary.getGeneratedKeys();
                    while (resultSalary.next()) {
                        salary1 = salaryRowMapper.mapRow(resultSalary);
                    }
                } catch (PSQLException e) {
                    salary1 = findSalary(salary);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return salary1;
        }

        private Salary findSalary(BigDecimal title) {
            Salary salary = null;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id as salary_id, value as salary_value from salary where value = ?");
                preparedStatement.setBigDecimal(1, title);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    salary = salaryRowMapper.mapRow(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return salary;
        }

        private Salary findSalary(Long id) {
            Salary salary = null;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id as salary_id, value as salary_value from salary where id = ?");
                preparedStatement.setLong(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    salary = salaryRowMapper.mapRow(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return salary;
        }

        public Organization addOrganization(String title) {
            Organization organization = null;
            try {
                String insertAndGetValues = "insert into organizations(title) values(?) returning id as org_id, title as org_title";
                PreparedStatement addOrganization = connection.prepareStatement(insertAndGetValues,
                    Statement.RETURN_GENERATED_KEYS);

                addOrganization.setString(1, title);

                try {
                    addOrganization.execute();
                    ResultSet resultSetOrg = addOrganization.getGeneratedKeys();
                    while (resultSetOrg.next()) {
                        organization = organizationRowMapper.mapRow(resultSetOrg);
                    }
                } catch (PSQLException e) {
                    organization = findOrganization(title);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return organization;
        }

        public Organization findOrganization(final String title) throws SQLException {
            PreparedStatement preparedStatement =
                connection.prepareStatement("select id as org_id, title as org_title from organizations where title = ? ");
            preparedStatement.setString(1, title);

            PreparedStatement preparedStatementEmployees =
                connection.prepareStatement("select id as empl_id, ename as empl_title, salary_id from employees where org_id = ? ");
            ResultSet resultSet = preparedStatement.executeQuery();

            Organization org = null;
            while (resultSet.next()) {
                org = organizationRowMapper.mapRow(resultSet);

                preparedStatementEmployees.setLong(1, resultSet.getLong("org_id"));
                ResultSet rs = preparedStatementEmployees.executeQuery();
                Set<Employee> employees = new HashSet<>();
                while (rs.next()) {
                    Employee employee = employeeRowMapper.mapRow(rs);
                    Salary salary = findSalary(rs.getLong("salary_id"));
                    employee.setSalary(salary);
                    employees.add(employee);
                }
                org.setEmployees(employees);
            }
            return org;
        }

        public Employee get(final String name) {

            try {
                PreparedStatement preparedStatement =
                    connection.prepareStatement("select id as empl_id, ename as empl_title, salary_id from employees where ename = ? ");
                preparedStatement.setString(1, name);

                ResultSet emplResult = preparedStatement.executeQuery();

                Employee employee = null;
                while (emplResult.next()) {
                    employee = employeeRowMapper.mapRow(emplResult);
                    Salary salary = findSalary(emplResult.getLong("salary_id"));
                    employee.setSalary(salary);
                }
                return employee;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public Employee delete(final String name) {

            try {

                Employee employee = get(name);
                PreparedStatement stmt = connection.prepareStatement("delete from employees where ename = ?");
                stmt.setString(1, name);
                if (stmt.executeUpdate() >= 1) {
                    return employee;
                } else {
                    System.out.println("Не получилось удалить работника " + employee);
                    return new Employee();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public Employee update(Employee newEmpl) {

            String sql = "UPDATE employees SET ename=?, salary_id=?  WHERE id=?";


            Employee employee;
            try {

                Salary salary = addSalary(newEmpl.getSalary().getValue());


                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, newEmpl.getName());
                stmt.setLong(2, salary.getId());

                stmt.setLong(3, newEmpl.getId());
                if (stmt.executeUpdate() >= 1) {
                    employee = get(newEmpl.getName());
                    return employee;
                } else {
                    System.out.println("Не получилось обновить работника " + newEmpl);
                    return new Employee();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
