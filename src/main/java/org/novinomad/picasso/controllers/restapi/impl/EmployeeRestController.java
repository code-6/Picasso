package org.novinomad.picasso.controllers.restapi.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.entities.domain.impl.Employee;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.services.impl.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employee")
public class EmployeeRestController implements ICrud<Employee> {

    final EmployeeService employeeService;

    @Override
    @PostMapping
    public Employee save(Employee employee) throws BaseException {
        return employeeService.save(employee);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws BaseException {
        employeeService.delete(id);
    }

    @GetMapping("/{id}")
    public Employee fetch(@PathVariable("id") Long id) {
        return employeeService.get(id).orElseThrow(()->new NoSuchElementException("Employee not found by id: " + id));
    }

    @Override
    @GetMapping
    public List<Employee> get() {
        return employeeService.get();
    }

    @GetMapping(params = "types")
    public List<Employee> get(@RequestParam("types") Employee.Type ... types) {
        return employeeService.get(types);
    }
}
