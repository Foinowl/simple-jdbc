package org.example.db.service;

public interface SalaryService<T> {
    long create(T t);

    long update(T t);

    T findById(long id);
}
