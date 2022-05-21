package org.example.db.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Organization {
  private Long id;

  @JsonProperty("title")
  private String title;

  public Organization(String title) {
    this.title = title;
  }

  public Organization() {
  }

  private Set<Employee> employees = new HashSet<>();

  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public Set<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(Set<Employee> employees) {
    this.employees = employees;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organization that = (Organization) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public String toString() {
    return "Organization{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", employees=" + employees +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }
}
