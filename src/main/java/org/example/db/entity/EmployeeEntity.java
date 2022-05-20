package org.example.db.entity;

public class EmployeeEntity {
    private long id;

    private String name;

    private long salaryId;

    private long orgId;

    public EmployeeEntity(long id, String name, long salaryId, long orgId) {
        this.id = id;
        this.name = name;
        this.salaryId = salaryId;
        this.orgId = orgId;
    }

    public EmployeeEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(long salaryId) {
        this.salaryId = salaryId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
}
