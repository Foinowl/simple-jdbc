package org.example.db.service;

import java.math.BigDecimal;

public interface SalaryService<T> {
    long create(T t);

    long update(T t);

    T findById(long id);

    T findByValue(BigDecimal value);
}
