package org.novinomad.picasso.repositories;

import org.novinomad.picasso.domain.entities.impl.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByTypeIn(List<Employee.Type> types);

    List<Employee> findAllByIdIn(List<Long> ids);
}
