package org.example.db.dao;

import java.util.List;

public interface DAO<T> {

    long save(T t);

    void update(T t);

    boolean delete(long id);

    T findById(long id);

    List<T> findAll();
}
