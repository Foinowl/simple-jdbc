package org.example.db.dao;

import java.util.List;
import org.example.db.entity.OrganizationEntity;
import org.example.db.mappers.RowMapper;

public class OrganizationDao<T extends OrganizationEntity> extends TemplateExecutor<T> implements DAO<T>{

    public OrganizationDao(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public long save(OrganizationEntity organizationEntity) {
        return 0;
    }

    @Override
    public void update(OrganizationEntity organizationEntity) {

    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public T findById(long id) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return null;
    }
}
