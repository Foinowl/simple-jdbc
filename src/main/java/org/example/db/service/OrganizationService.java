package org.example.db.service;

import java.util.List;

public interface OrganizationService<T> {
    long create(T t);

    long update(T t);

    T findById(long id);

    T findByName(String name);

    List<T> findAll();

    T addEmployeeToModel(T t);
}
