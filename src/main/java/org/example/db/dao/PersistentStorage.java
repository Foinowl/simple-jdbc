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
                    connection.prepareStatement("select id, ename, salary_id, org_id from employees");
                PreparedStatement statement1 = connection.prepareStatement("select id, value from salary where id = ?");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setName(resultSet.getString("ename"));
                    employee.setId(resultSet.getLong("id"));
                    Long salaryId = resultSet.getLong("salary_id");

                    Salary salary = new Salary();
                    statement1.setLong(1, salaryId);
                    ResultSet salaryResultSet = statement1.executeQuery();
                    while (salaryResultSet.next()) {
                        salary.setValue(salaryResultSet.getBigDecimal("value"));
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
            PreparedStatement addSalary = connection.prepareStatement("insert into salary(value) values(?)",
                Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addOrganization =
                connection.prepareStatement("insert into organizations(title) values(?)",
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addEmployee =
                connection.prepareStatement("insert into employees(ename, salary_id, org_id) values (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            addSalary.setLong(1, empSalary.longValueExact());
            addSalary.execute();

            Long salaryId = null;
            ResultSet salaryIdSet = addSalary.getGeneratedKeys();
            while (salaryIdSet.next()) {
                salaryId = salaryIdSet.getLong("id");
            }

            addOrganization.setString(1, orgTitle);

            Long orgId = null;
            try{
                addOrganization.execute();
                ResultSet orgIdSet = addOrganization.getGeneratedKeys();
                while (orgIdSet.next()) {
                    orgId = orgIdSet.getLong("id");
                }   
            } catch (PSQLException e){
                orgId = findOrganization(orgTitle).getId();
            }

            addEmployee.setString(1, emplName);
            addEmployee.setLong(2, salaryId);
            addEmployee.setLong(3, orgId);

            addEmployee.execute();
        }

        public Organization findOrganization(final String title) throws SQLException {
            PreparedStatement preparedStatement =
                connection.prepareStatement("select id, title from organizations where title = ? ");
            preparedStatement.setString(1, title);

            PreparedStatement preparedStatementEm =
                connection.prepareStatement("select ename from employees where org_id = ? ");
            ResultSet resultSet = preparedStatement.executeQuery();

            Organization org = null;
            while (resultSet.next()) {
                String name = resultSet.getString("title");
                org = new Organization(name);
                org.setId(resultSet.getLong("id"));
                preparedStatementEm.setLong(1, resultSet.getLong("id"));
                ResultSet rs = preparedStatementEm.executeQuery();
                Set<Employee> employees = new HashSet<>();
                while (rs.next()) {
                    Employee employee = get(rs.getString("ename"));
                    employees.add(employee);
                }
                org.setEmployees(employees);
            }
            return org;
        }

        public Employee get(final String name) {

            try {
                PreparedStatement preparedStatement =
                    connection.prepareStatement("select id, ename, salary_id from employees where ename = ? ");
                preparedStatement.setString(1, name);

                PreparedStatement salaryStatement =
                    connection.prepareStatement("select id, value from salary where id = ?");

                ResultSet emplResult = preparedStatement.executeQuery();

                Employee employee = new Employee();
                while (emplResult.next()) {
                    String names = emplResult.getString("ename");
                    long salaryId = emplResult.getLong("salary_id");

                    employee.setName(names);
                    employee.setId(emplResult.getLong("id"));
                    salaryStatement.setLong(1, salaryId);
                    ResultSet salaryRS = salaryStatement.executeQuery();
                    BigDecimal bigDecimal = null;
                    Salary salary = new Salary();
                    while (salaryRS.next()) {
                        salary.setValue(salaryRS.getBigDecimal("value"));
                        salary.setId(salaryRS.getLong("id"));
                    }
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
                PreparedStatement addSalary = connection.prepareStatement("insert into salary(value) values(?)",
                    Statement.RETURN_GENERATED_KEYS);
                addSalary.setBigDecimal(1, newEmpl.getSalary().getValue());

                addSalary.execute();

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, newEmpl.getName());
                addSalary.getGeneratedKeys().next();
                stmt.setLong(2, addSalary.getGeneratedKeys().getLong("id"));

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
