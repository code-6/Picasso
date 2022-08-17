package org.novinomad.picasso.services.conversion.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.services.impl.EmployeeService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EmployeeFormatter implements Formatter<Employee> {

    final ObjectMapper objectMapper;
    final EmployeeService employeeService;

    @Override
    public Employee parse(String employeeString, Locale locale) throws ParseException {
        if(NumberUtils.isCreatable(employeeString)) {
            long employeeId = Long.parseLong(employeeString);
            return employeeService.get(employeeId)
                    .orElseThrow(() -> new ParseException("Employee parse failed because there is no tour with id: " + employeeId, 0));
        } else {
            try {
                return objectMapper.readValue(employeeString, Employee.class);
            } catch (JsonProcessingException e) {
                throw new ParseException("Tour parse failed because invalid JSON: " + employeeString + " " + e.getMessage(), 0);
            }
        }
    }

    @Override
    public String print(Employee employee, Locale locale) {
        try {
            return objectMapper.writeValueAsString(employee);
        } catch (JsonProcessingException e) {
            return employee.toStringFull();
        }
    }
}
