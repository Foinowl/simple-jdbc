package org.example.db.service;

/*
* E - entity
* M - model
* */
public interface ConvertService<E, M>{

    M convertToModel(E e);

    E convertToEntity(M m);
}
