package org.example.db.entity;

import java.math.BigDecimal;

public class SalaryEntity {
    private long id;

    private BigDecimal value;

    public SalaryEntity(long id, BigDecimal value) {
        this.id = id;
        this.value = value;
    }

    public SalaryEntity() {


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
