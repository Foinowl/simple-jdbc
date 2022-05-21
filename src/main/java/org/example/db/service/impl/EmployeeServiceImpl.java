package org.example.db.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.example.db.dao.EmployeeDao;
import org.example.db.dao.RepositoryFactory;
import org.example.db.entity.EmployeeEntity;
import org.example.db.model.Employee;
import org.example.db.service.ConvertService;
import org.example.db.service.EmployeeService;
import org.example.db.service.convert.EmployeeConverter;

public class EmployeeServiceImpl implements EmployeeService<Employee> {
    private final ConvertService<EmployeeEntity, Employee> convertService = new EmployeeConverter();

    private final EmployeeDao employeeDao = RepositoryFactory.getInstance().getEmployeeDao();
    @Override
    public long create(Employee employee) {
       return employeeDao.save(convertService.convertToEntity(employee));
    }

    @Override
    public long update(Employee employee) {
        return employeeDao.update(convertService.convertToEntity(employee));
    }

    @Override
    public Employee findById(long id) {
        return convertService.convertToModel(employeeDao.findById(id));
    }

    @Override
    public Employee findByName(String name) {
        return convertService.convertToModel(employeeDao.findByName(name));
    }

    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll().stream().map(convertService::convertToModel).collect(Collectors.toList());
    }

    @Override
    public boolean delete(Employee employee) {
        return employeeDao.delete(employee.getId());
    }
}
