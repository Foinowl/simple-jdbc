package org.example.db.dao;

import java.util.List;
import org.example.db.entity.SalaryEntity;
import org.example.db.mappers.RowMapper;

public class SalaryDao<T extends SalaryEntity> extends TemplateExecutor<T> implements DAO<T>{
    public SalaryDao(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public long save(SalaryEntity salaryEntity) {
        return 0;
    }

    @Override
    public void update(SalaryEntity salaryEntity) {

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
