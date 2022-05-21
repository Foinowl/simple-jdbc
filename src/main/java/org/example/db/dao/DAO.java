package org.example.db.dao;

import java.util.List;

public interface DAO<T> {

    long save(T t);

    long update(T t);

    boolean delete(long id);

    boolean delete(String name);

    T findById(long id);

    List<T> findAll();
}
