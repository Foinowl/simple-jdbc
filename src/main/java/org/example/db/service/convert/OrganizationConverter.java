package org.example.db.service.convert;

import java.util.Set;
import java.util.stream.Collectors;
import org.example.db.dao.EmployeeDao;
import org.example.db.dao.RepositoryFactory;
import org.example.db.entity.EmployeeEntity;
import org.example.db.entity.OrganizationEntity;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.service.ConvertService;

public class OrganizationConverter implements ConvertService<OrganizationEntity, Organization> {
    private final static ConvertService<EmployeeEntity, Employee> employeeConverter = new EmployeeConverter();
    private final static EmployeeDao employeeDao = RepositoryFactory.getInstance().getEmployeeDao();

    @Override
    public Organization convertToModel(OrganizationEntity entity) {
        Organization model = new Organization();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        return model;
    }

    @Override
    public OrganizationEntity convertToEntity(Organization organization) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(organization.getId());
        entity.setTitle(organization.getTitle());
        return entity;
    }
}
