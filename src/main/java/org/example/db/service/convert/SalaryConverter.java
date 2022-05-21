package org.example.db.service.convert;

import org.example.db.entity.SalaryEntity;
import org.example.db.model.Salary;
import org.example.db.service.ConvertService;

public class SalaryConverter implements ConvertService<SalaryEntity, Salary> {
    @Override
    public Salary convertToModel(SalaryEntity salaryEntity) {
        Salary model = new Salary();
        model.setId(salaryEntity.getId());
        model.setValue(salaryEntity.getValue());
        return model;
    }

    @Override
    public SalaryEntity convertToEntity(Salary salary) {
        SalaryEntity entity = new SalaryEntity();
        entity.setId(salary.getId());
        entity.setValue(salary.getValue());
        return entity;
    }

}
