package org.example.db.dao;

import java.math.BigDecimal;
import java.util.List;
import org.example.db.entity.SalaryEntity;
import org.example.db.mappers.factory.FactoryMapper;

public class SalaryDao extends TemplateExecutor<SalaryEntity> implements DAO<SalaryEntity> {

    private final String sqlSalaryFindById = "select id, value from salary where id = ?";
    private final String sqlSalaryFindByValue = "select id, valie from salary where value = ?";

    private final String sqlSalaryFindAll = "select id, value from salary";

    private final String sqlSalarySave = "insert into salary (value) values(?)";

    private final String sqlSalaryUpdate = "update salary set value = ? where id = ?";

    private final String sqlSalaryDelete = "delete from salary where id = ?";

    public SalaryDao() {
        super(FactoryMapper.getInstance().getMapperSalaryEntity());
    }

    @Override
    public long save(SalaryEntity salaryEntity) {
        return insert(sqlSalarySave, salaryEntity.getValue());
    }

    @Override
    public long update(SalaryEntity salaryEntity) {
        return update(sqlSalaryUpdate, salaryEntity.getValue(), salaryEntity.getId());
    }

    @Override
    public boolean delete(long id) {
        return delete(sqlSalaryDelete, id);
    }

    @Override
    public SalaryEntity findById(long id) {
        return selectForEntity(sqlSalaryFindById, id).orElse(null);
    }

    @Override
    public List<SalaryEntity> findAll() {
        return select(sqlSalaryFindAll);
    }

    public SalaryEntity findByValue(BigDecimal value) {
        return selectForEntity(sqlSalaryFindByValue, value).orElse(null);
    }
}
