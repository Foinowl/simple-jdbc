package org.example.db.entity;

public class OrganizationEntity {
    private long id;

    private String title;

    public OrganizationEntity(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public OrganizationEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
