package org.example.db.service;

import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.model.Salary;
import org.example.db.service.impl.EmployeeServiceImpl;
import org.example.db.service.impl.OrganizationServiceImpl;
import org.example.db.service.impl.SalaryServiceImpl;

public class ServiceFactory {
    private final static ServiceFactory INSTANCE = new ServiceFactory();

    private final EmployeeService<Employee> employeeService = new EmployeeServiceImpl();
    private final SalaryService<Salary> salaryService = new SalaryServiceImpl();
    private final OrganizationService<Organization> organizationService = new OrganizationServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public EmployeeService<Employee> getEmployeeService() {
        return employeeService;
    }

    public SalaryService<Salary> getSalaryService() {
        return salaryService;
    }

    public OrganizationService<Organization> getOrganizationService() {
        return organizationService;
    }
}
