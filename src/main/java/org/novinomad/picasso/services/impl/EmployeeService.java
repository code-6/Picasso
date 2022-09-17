package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Employee;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.repositories.jpa.EmployeeRepository;
import org.novinomad.picasso.services.IEmployeeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service("employeeService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeService implements IEmployeeService {

    final EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) throws BaseException {
        try {
            Employee savedEmployee = employeeRepository.save(employee);
            log.debug("saved {}", employee);
            return savedEmployee;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", employee, e.getMessage(), e);
            throw new BaseException(e, "unable to create: {} because: {}", employee, e.getMessage());
        }
    }

    @Transactional
    public List<Employee> save(Collection<Employee> employees) {
        List<Employee> savedEmployees = new ArrayList<>();
        employees.forEach(employee -> {
            try {
                savedEmployees.add(save(employee));
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if (savedEmployees.size() != employees.size())
            log.warn("not all Employees are saved. To be saved: {} saved: {}", employees.size(), savedEmployees.size());

        return savedEmployees;
    }

    @Override
    public void delete(Long id) throws BaseException {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Employee with id: {} because: {}", id, e.getMessage(), e);
            throw new BaseException(e, "unable to delete Employee with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedEmployees = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedEmployees.add(id);
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if (deletedEmployees.size() != ids.size())
            log.warn("not all Employees are deleted. To be deleted: {} deleted: {}", deletedEmployees.size(), ids.size());

        return deletedEmployees;
    }

    @Transactional
    public List<Long> deleteAll(Collection<Employee> employees) {
        List<Long> ids = employees.stream().map(Employee::getId).toList();
        return delete(ids);
    }

    @Override
    public Optional<Employee> get(Long id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            log.error("unable to get Employee by id: {} because: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> get() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> get(Long... ids) {
        return employeeRepository.findAllByIdIn(Arrays.asList(ids));
    }

    @Override
    public List<Employee> get(List<Employee.Type> types) {
        return employeeRepository.findAllByTypeIn(types);
    }
}
