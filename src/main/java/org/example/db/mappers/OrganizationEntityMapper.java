package org.example.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.entity.OrganizationEntity;

public class OrganizationEntityMapper implements RowMapper<OrganizationEntity>{
    @Override
    public OrganizationEntity mapRow(ResultSet resultSet) throws SQLException {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setTitle(resultSet.getString("title"));
        return entity;
    }
}
