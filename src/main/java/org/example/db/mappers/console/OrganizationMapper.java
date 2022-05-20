package org.example.db.mappers.console;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.db.mappers.RowMapper;
import org.example.db.model.Organization;

public class OrganizationMapper implements RowMapper<Organization> {
    @Override
    public Organization mapRow(final ResultSet resultSet) throws SQLException {
        Organization organization = new Organization();
        organization.setId(resultSet.getLong("org_id"));
        organization.setTitle(resultSet.getString("org_title"));
        return organization;
    }
}
