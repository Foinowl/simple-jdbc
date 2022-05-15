package org.example.db.model;

public class Employee {
  private String name;
  private Salary salary;

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
}
