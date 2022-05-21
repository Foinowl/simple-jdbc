package org.example.db.service.convert;

import org.example.db.entity.EmployeeEntity;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.model.Salary;
import org.example.db.service.ConvertService;
import org.example.db.service.OrganizationService;
import org.example.db.service.SalaryService;
import org.example.db.service.impl.OrganizationServiceImpl;
import org.example.db.service.impl.SalaryServiceImpl;

public class EmployeeConverter implements ConvertService<EmployeeEntity, Employee> {
    private final SalaryService<Salary> salaryService = new SalaryServiceImpl();

    private final OrganizationService<Organization> organizationService = new OrganizationServiceImpl();

    @Override
    public Employee convertToModel(EmployeeEntity employeeEntity) {
        Employee model = new Employee();
        model.setId(employeeEntity.getId());
        model.setName(employeeEntity.getName());
        model.setSalary(salaryService.findById(employeeEntity.getSalaryId()));
        model.setOrganization(organizationService.findById(employeeEntity.getOrgId()));
        return model;
    }

    @Override
    public EmployeeEntity convertToEntity(Employee employee) {

        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(employee.getId());
        entity.setName(employee.getName());
        entity.setSalaryId(employee.getSalary().getId());
        entity.setOrgId(employee.getOrganization().getId());
        return entity;
    }
}