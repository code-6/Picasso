package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Employee;

import java.util.List;

public interface IEmployeeService extends ICrud<Employee> {

    List<Employee> get(Employee.Type... types);
    default List<Employee> get(List<Employee.Type> types) {
        return get( types.toArray(Employee.Type[] :: new) );
    }
}
