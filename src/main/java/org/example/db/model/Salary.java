package org.example.db.model;

import java.math.BigDecimal;

public class Salary {
  private Long id;
  private BigDecimal value;

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }
}
