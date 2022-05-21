package org.example.db.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.db.dao.EmployeeDao;
import org.example.db.dao.OrganizationDao;
import org.example.db.dao.RepositoryFactory;
import org.example.db.entity.EmployeeEntity;
import org.example.db.entity.OrganizationEntity;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.service.ConvertService;
import org.example.db.service.OrganizationService;
import org.example.db.service.convert.EmployeeConverter;
import org.example.db.service.convert.OrganizationConverter;

public class OrganizationServiceImpl implements OrganizationService<Organization> {

    private final static OrganizationDao organizationDao = RepositoryFactory.getInstance().getOrganizationDao();

    private final static ConvertService<EmployeeEntity, Employee> employeeConverter = new EmployeeConverter();

    private final static EmployeeDao employeeDao = RepositoryFactory.getInstance().getEmployeeDao();

    private final static ConvertService<OrganizationEntity, Organization> organizationConverter =
        new OrganizationConverter();

    @Override
    public long create(Organization organization) {
        OrganizationEntity entity = organizationDao.findByName(organization.getTitle());
        if (entity == null) {
            return organizationDao.save(organizationConverter.convertToEntity(organization));
        }
        return entity.getId();
    }

    @Override
    public long update(Organization organization) {
        return organizationDao.update(organizationConverter.convertToEntity(organization));
    }

    @Override
    public Organization findById(long id) {
        return organizationConverter.convertToModel(organizationDao.findById(id));
    }

    @Override
    public Organization findByName(String name) {
        return organizationConverter.convertToModel(organizationDao.findByName(name));
    }

    @Override
    public List<Organization> findAll() {
        return organizationDao.findAll().stream()
            .map(organizationConverter::convertToModel)
            .map(this::addEmployeeToModel)
            .collect(Collectors.toList());
    }

    @Override
    public Organization addEmployeeToModel(Organization model) {
        Set<Employee> entitys =
            employeeDao.findByOrgId(model.getId()).stream().map(employeeConverter::convertToModel).collect(
                Collectors.toSet());
        model.setEmployees(entitys);
        return model;
    }
}
