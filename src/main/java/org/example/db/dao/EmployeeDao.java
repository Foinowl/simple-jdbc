package org.example.db.dao;

import java.util.List;
import org.example.db.entity.EmployeeEntity;
import org.example.db.mappers.RowMapper;

public class EmployeeDao<T extends EmployeeEntity> extends TemplateExecutor<T> implements DAO<T> {

    public EmployeeDao(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public long save(EmployeeEntity employeeEntity) {
        return 0;
    }

    @Override
    public void update(EmployeeEntity employeeEntity) {

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
