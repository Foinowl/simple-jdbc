package org.example.db.dao;

import java.util.List;
import org.example.db.entity.EmployeeEntity;
import org.example.db.mappers.factory.FactoryMapper;

public class EmployeeDao extends TemplateExecutor<EmployeeEntity> implements DAO<EmployeeEntity> {

    private final String sqlEmployeeFindById = "select * from employees where id = ?";

    private final String sqlEmployeeFindByName = "select * from employees where ename = ?";

    private final String sqlEmployeeFindAll = "select * from employees";

    private final String sqlEmployeeFindByOrgId = "select * from employees where org_id = ?";

    private final String sqlEmployeeSave = "insert into employees (ename, salary_id, org_id) values(?, ?, ?)";

    private final String sqlEmployeeUpdate = "update employees set ename = ?, salary_id = ?, org_id = ?  where id = ?";

    private final String sqlEmployeeDelete = "delete from employees where id = ?";

    public EmployeeDao() {
        super(FactoryMapper.getInstance().getMapperEmployeeEntity());
    }

    @Override
    public long save(EmployeeEntity employeeEntity) {
        return insert(sqlEmployeeSave, employeeEntity.getName(), employeeEntity.getSalaryId(),
            employeeEntity.getOrgId());

    }

    @Override
    public long update(EmployeeEntity employeeEntity) {
        return update(sqlEmployeeUpdate, employeeEntity.getName(), employeeEntity.getSalaryId(), employeeEntity.getOrgId(),
            employeeEntity.getId());

    }

    @Override
    public boolean delete(long id) {
        return delete(sqlEmployeeDelete, id);
    }

    @Override
    public EmployeeEntity findById(long id) {
        return selectForEntity(sqlEmployeeFindById, id).orElse(null);
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return select(sqlEmployeeFindAll);
    }

    public List<EmployeeEntity> findByOrgId(long id) {
        return select(sqlEmployeeFindByOrgId, id);
    }

    public EmployeeEntity findByName(String name) {
        return selectForEntity(sqlEmployeeFindByName, name).orElse(null);
    }
}
