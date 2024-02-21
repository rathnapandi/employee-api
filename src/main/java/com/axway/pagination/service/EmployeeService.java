package com.axway.pagination.service;

import com.axway.pagination.model.Employee;
import com.axway.pagination.repo.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }


}
