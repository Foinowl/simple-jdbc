package org.example.db.dao;

import java.util.List;

// TODO: Реализовать основые методы для реализации общего API из задания
public interface DAO<T> {

    long save(T t);

    void update(T t);

    boolean delete(long id);

    T findById(long id);

    List<T> findAll();
}
