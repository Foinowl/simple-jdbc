package org.example.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return TransactionWrapper.getInstance().findOrganization(title);
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

        private final Connection connection;

        private final Callback<Employee> callbackEmployee = (ResultSet rs, Employee employee) -> {
            Salary salary = findSalary(rs.getLong("salary_id"));
            employee.setSalary(salary);
            return employee;
        };

/*        Можем вынести класс в отдельный класс, который будет подтягивать конф.настройки для бд
          И составлять нужный урл для подключения бд.
          Так же можно создать пул из всех соединений.
        */
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

            String getEmployees = "select id as empl_id, ename as empl_title, salary_id, org_id from employees";
            Set<Employee> result;

            try {
                result =
                    JdbcTemplate.select(connection, getEmployees, UtilsHandler.SET_EMPLOYEES_HANDLER, callbackEmployee);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return result;
        }

        public void add(String orgTitle, String emplName, BigDecimal empSalary) throws SQLException {

            try {
                Salary salary = addSalary(empSalary);

                Organization organization = addOrganization(orgTitle);
                JdbcTemplate.insert(connection, "insert into employees(ename, salary_id, org_id) values (?,?,?)",
                    (rs, o) -> null, (r, p) -> null, emplName, salary.getId(), organization.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private Salary addSalary(BigDecimal salary) {
            String insertSalarySql =
                "insert into salary(value) values(?) returning id as salary_id, value as salary_value";

            Salary salary1;
            try {
                salary1 = JdbcTemplate.insert(connection, insertSalarySql, UtilsHandler.ONE_SALARY_HANDLER,
                    (ResultSet rs, Salary s) -> s, salary);
            } catch (PSQLException e) {
                salary1 = findSalary(salary);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return salary1;
        }

        private Salary findSalary(BigDecimal title) {
            String sqlSalary = "select id as salary_id, value as salary_value from salary where value = ?";

            try {
                return JdbcTemplate.select(connection, sqlSalary, UtilsHandler.ONE_SALARY_HANDLER,
                    (ResultSet rs, Salary s) -> s, title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private Salary findSalary(Long id) {
            String sqlSalary = "select id as salary_id, value as salary_value from salary where id = ?";

            Salary salary;
            try {
                salary = JdbcTemplate.select(connection, sqlSalary, UtilsHandler.ONE_SALARY_HANDLER,
                    (ResultSet rs, Salary s) -> s, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return salary;
        }

        public Organization addOrganization(String title) {
            String insertOrganizationSql =
                "insert into organizations(title) values(?) returning id as org_id, title as org_title";

            Organization organization;
            try {
                organization =
                    JdbcTemplate.insert(connection, insertOrganizationSql, UtilsHandler.ONE_ORGANIZATION_HANDLER,
                        (ResultSet rs, Organization o) -> o, title);
            } catch (PSQLException e) {
                organization = findOrganization(title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return organization;
        }

        public Organization findOrganization(final String title) {

            String sqlSelectOrg = "select id as org_id, title as org_title from organizations where title = ? ";

            String sqlSelectEmpl =
                "select id as empl_id, ename as empl_title, salary_id from employees where org_id = ? ";

            Organization organization;
            try {
                Callback<Organization> cbOrg = (ResultSet rs, Organization org) -> {
                    Set<Employee> sets =
                        JdbcTemplate.select(connection, sqlSelectEmpl, UtilsHandler.SET_EMPLOYEES_HANDLER,
                            callbackEmployee,
                            rs.getLong("org_id"));
                    org.setEmployees(sets);
                    return org;
                };
                organization =
                    JdbcTemplate.select(connection, sqlSelectOrg, UtilsHandler.ONE_ORGANIZATION_HANDLER, cbOrg,
                        title);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return organization;
        }

        public Employee get(final String name) {

            String sqlGetEmployee =
                "select id as empl_id, ename as empl_title, salary_id from employees where ename = ? ";
            Employee employee;
            try {
                employee =
                    JdbcTemplate.select(connection, sqlGetEmployee, UtilsHandler.ONE_EMPLOYEE_HANDLER, callbackEmployee,
                        name);
                return employee;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public Employee delete(final String name) {

            try {
                Employee employee = get(name);
                int result = JdbcTemplate.update(connection, "delete from employees where ename=?", name);
                if (result >= 1) {
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
            Employee employee = null;
            try {
                Salary salary = addSalary(newEmpl.getSalary().getValue());

                int result = JdbcTemplate.update(connection, "UPDATE employees SET ename=?, salary_id=?  WHERE id=?",
                    newEmpl.getName(), salary.getId(), newEmpl.getId());

                if (result >= 1) {
                    employee = get(newEmpl.getName());
                } else {
                    System.out.println("Не получилось обновить работника " + newEmpl);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return employee;
        }
    }
}
