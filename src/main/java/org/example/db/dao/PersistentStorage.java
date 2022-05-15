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
    return null;
  }

  @Override
  public Employee get(final String name) {
    return new Employee();
  }

  @Override
  public Set<Employee> listAll() {
    return null;
  }

  @Override
  public Employee delete(final String name) {
    return null;
  }

  @Override
  public Employee update(final Employee newOne) {
    return null;
  }

  public static class TransactionWrapper {
        private static final TransactionWrapper instance = new TransactionWrapper();

        private Connection connection;

        private TransactionWrapper() {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres";

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
            addOrganization.execute();

            Long orgId = null;
            ResultSet orgIdSet = addOrganization.getGeneratedKeys();
            while (orgIdSet.next()) {
                orgId = orgIdSet.getLong("id");
            }

            addEmployee.setString(1, emplName);
            addEmployee.setLong(2, salaryId);
            addEmployee.setLong(3, orgId);

            addEmployee.execute();
        }
    }

}
