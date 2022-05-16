package org.example.db.dao;

import java.util.Set;
import org.example.db.model.Employee;
import org.example.db.model.Organization;

/**
 * Интерфейс, декларирующий возможные методы работы с данными в хранилище.
 * <p>
 * В данном пример присутствуют две реализации данного интерфейса:
 * 1. InmemoryStorageService - для хранения данных в оперативной памяти (в Map)
 * 2. PersistentStorage - для хранения данных в базе данных
 */

/*
    * Можно разбить данный интерфейс на несколько.
    * Сделать общий интерфейс для круд операций, параметризированный по классу из package model
    * Далее от общего интерфейса можно наследоваться и дополнять новыми действиями.
    * 1. DAO - интерфейс(get, delete, update, create)
    * 2. OrganizationDao - наследуется от DAO. Любые классы имплементирующие данный интерфейс
    * могут использовать разными хранилищами данными.
* */
public interface StorageService {

    Organization findOrganization(String title);

    void add(Organization org, Employee employee);

    Employee get(String name);

    Set<Employee> listAll();

    Employee delete(String name);

    Employee update(Employee newOne);
}
