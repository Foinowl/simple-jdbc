package org.example.db.model;

public class Employee {
  private Long id;

  private String name;
  private Salary salary;

  private Organization organization;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Salary getSalary() {
    return salary;
  }

  public void setSalary(Salary salary) {
    this.salary = salary;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", salary=" + salary +
        '}';
  }
}
