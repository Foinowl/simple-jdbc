package org.example.db.dao.console;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.db.model.Employee;
import org.example.db.model.Organization;

public class InmemoryStorageService implements StorageService {

    public static Set<Organization> db = new HashSet<>();

    private static StorageService instance = new InmemoryStorageService();

    private InmemoryStorageService() {
    }

    public static StorageService getInstance() {
        return instance;
    }

    @Override
    public Organization findOrganization(String title) {
        return db.stream().filter(o -> o.getTitle().equals(title)).findFirst().orElse(new Organization(title));
    }

    @Override
    public void add(Organization org, Employee employee) {
        org.getEmployees().add(employee);
        db.add(org);
    }

    @Override
    public Employee get(String name) {
        return null;
    }

    @Override
    public Set<Employee> listAll() {
        return db.stream().flatMap(o -> o.getEmployees().stream()).collect(Collectors.toSet());
    }

    @Override
    public Employee delete(String name) {
        return null;
    }

    @Override
    public Employee update(Employee newOne) {
        return null;
    }
}
