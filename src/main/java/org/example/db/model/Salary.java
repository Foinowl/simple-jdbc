package org.example.db.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Salary {
  private Long id;
  @JsonProperty("value")
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

  @Override
  public String toString() {
    return "Salary{" +
        "id=" + id +
        ", value=" + value +
        '}';
  }
}