package org.example.db.mappers.factory;

import org.example.db.entity.EmployeeEntity;
import org.example.db.entity.OrganizationEntity;
import org.example.db.entity.SalaryEntity;
import org.example.db.mappers.EmployeeEntityMapper;
import org.example.db.mappers.OrganizationEntityMapper;
import org.example.db.mappers.RowMapper;
import org.example.db.mappers.SalaryEntityMapper;

public class FactoryMapper {
    private static final FactoryMapper INSTANCE = new FactoryMapper();


    private final RowMapper<SalaryEntity> mapperSalaryEntity = new SalaryEntityMapper();

    private final RowMapper<OrganizationEntity> mapperOrganizationEntity = new OrganizationEntityMapper();

    private final RowMapper<EmployeeEntity> mapperEmployeeEntity = new EmployeeEntityMapper();


    private FactoryMapper() {

    }

    public static FactoryMapper getInstance() {
        return INSTANCE;
    }

    public RowMapper<SalaryEntity> getMapperSalaryEntity() {
        return mapperSalaryEntity;
    }

    public RowMapper<OrganizationEntity> getMapperOrganizationEntity() {
        return mapperOrganizationEntity;
    }

    public RowMapper<EmployeeEntity> getMapperEmployeeEntity() {
        return mapperEmployeeEntity;
    }
}
