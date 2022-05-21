package org.example.db.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
  private Long id;

  private String name;

  @JsonProperty("salary")
  private Salary salary;

  @JsonProperty("organization")
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

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
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
        ", organization=" + organization +
        '}';
  }
}