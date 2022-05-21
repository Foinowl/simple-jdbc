package org.example.db.dao;

import java.util.List;
import org.example.db.entity.OrganizationEntity;
import org.example.db.mappers.factory.FactoryMapper;

public class OrganizationDao extends TemplateExecutor<OrganizationEntity> implements DAO<OrganizationEntity> {
    private final String sqlOrganizationFindById = "select id, title from organizations where id = ?";

    private final String sqlOrganizationFindByName = "select id, title from organizations where title = ?";

    private final String sqlOrganizationFindAll = "select id, title from organizations";

    private final String sqlOrganizationSave = "insert into organizations (title) values(?)";

    private final String sqlOrganizationUpdate = "update organizations set title = ? where id = ?";

    private final String sqlOrganizationDelete = "delete from organizations where id = ?";
    public OrganizationDao() {
        super(FactoryMapper.getInstance().getMapperOrganizationEntity());
    }

    @Override
    public long save(OrganizationEntity organizationEntity) {
        return insert(sqlOrganizationSave, organizationEntity.getTitle());
    }

    @Override
    public long update(OrganizationEntity organizationEntity) {
        return update(sqlOrganizationUpdate, organizationEntity.getTitle(), organizationEntity.getId());
    }

    @Override
    public boolean delete(long id) {
        return delete(sqlOrganizationDelete, id);
    }

    @Override
    public boolean delete(String name) {
        return false;
    }

    @Override
    public OrganizationEntity findById(long id) {
        return selectForEntity(sqlOrganizationFindById, id).orElse(null);
    }

    @Override
    public List<OrganizationEntity> findAll() {
        return select(sqlOrganizationFindAll);
    }

    public OrganizationEntity findByName(String name) {
        return selectForEntity(sqlOrganizationFindByName, name).orElse(null);
    }
}
