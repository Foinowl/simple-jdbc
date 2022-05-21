package org.example.db.dao;

public class RepositoryFactory {
    private final SalaryDao salaryDao = new SalaryDao();

    private final OrganizationDao organizationDao = new OrganizationDao();

    private final EmployeeDao employeeDao = new EmployeeDao();

    private final static RepositoryFactory INSTANCE = new RepositoryFactory();

    public RepositoryFactory() {
    }

    public static RepositoryFactory getInstance() {
        return INSTANCE;
    }
    public SalaryDao getSalaryDao() {
        return salaryDao;
    }

    public OrganizationDao getOrganizationDao() {
        return organizationDao;
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }
}

