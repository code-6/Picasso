package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.entities.domain.impl.Employee;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IEmployeeService extends ICrud<Employee> {

    default List<Employee> get(Employee.Type... types) {
        return get(Arrays.asList(types));
    }

    default List<Employee> getOrEmpty(Employee.Type... types) {
        if(types == null || types.length == 0)
            return Collections.emptyList();

        return get(Arrays.asList(types));
    }

    default List<Employee> getOrEmpty(List<Employee.Type> types) {
        if(types == null || types.isEmpty())
            return Collections.emptyList();

        return get(types);
    }

    List<Employee> get(List<Employee.Type> types);

    List<Employee.Type> getEmployeeTypes();
}
