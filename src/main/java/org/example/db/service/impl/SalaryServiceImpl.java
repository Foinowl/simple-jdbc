package org.example.db.service.impl;

import java.math.BigDecimal;
import org.example.db.dao.RepositoryFactory;
import org.example.db.dao.SalaryDao;
import org.example.db.entity.SalaryEntity;
import org.example.db.model.Salary;
import org.example.db.service.ConvertService;
import org.example.db.service.SalaryService;
import org.example.db.service.convert.SalaryConverter;

public class SalaryServiceImpl implements SalaryService<Salary> {

    private final SalaryDao salaryDao = RepositoryFactory.getInstance().getSalaryDao();

    private final ConvertService<SalaryEntity, Salary> convertService = new SalaryConverter();
    @Override
    public long create(Salary salary) {
        SalaryEntity entity = salaryDao.findByValue(salary.getValue());
        if (entity == null) {
            return salaryDao.save(convertService.convertToEntity(salary));
        }
        return entity.getId();
    }

    @Override
    public long update(Salary salary) {
        return salaryDao.update(convertService.convertToEntity(salary));
    }

    @Override
    public Salary findById(long id) {
        return convertService.convertToModel(salaryDao.findById(id));
    }

    @Override
    public Salary findByValue(BigDecimal value) {
        return convertService.convertToModel(salaryDao.findByValue(value));
    }
}
