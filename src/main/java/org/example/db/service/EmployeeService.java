package org.example.db.service;

import java.util.List;

public interface EmployeeService<T> {
    long create(T t);

    long update(T t);

    T findById(long id);

    T findByName(String name);

    List<T> findAll();

    boolean delete(T t);

    boolean delete(String name);
}
