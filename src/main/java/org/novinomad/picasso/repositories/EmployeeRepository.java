package org.novinomad.picasso.repositories;

import org.novinomad.picasso.domain.entities.impl.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
