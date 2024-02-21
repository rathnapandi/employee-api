package com.axway.pagination;

import com.axway.pagination.bulk.Employee;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PaginationApplicationTests {

	@Test
	void contextLoads() {
        CsvMapper mapper = new CsvMapper();
        try(InputStream inputStream = new FileInputStream(new File("/Users/rnatarajan/Downloads/employees_350.csv"))){
            MappingIterator<Employee> mappingIterator =  mapper.readerWithTypedSchemaFor(com.axway.pagination.bulk.Employee.class).readValues(inputStream);
            List<Employee> employees = mappingIterator.readAll();
            List<com.axway.pagination.model.Employee> employeesDB = getEmployees(employees);
            System.out.println(employeesDB);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

    private static List<com.axway.pagination.model.Employee> getEmployees(List<com.axway.pagination.bulk.Employee> employees) {
        List<com.axway.pagination.model.Employee> employeesDB = new ArrayList<>();
        for(com.axway.pagination.bulk.Employee employee : employees){
            com.axway.pagination.model.Employee employeeDB = new com.axway.pagination.model.Employee();
            employeeDB.setFirstName(employee.getFirstName());
            employeeDB.setLastName(employee.getLastName());
            employeeDB.setEmail(employee.getEmail());
            employeeDB.setGender(employee.getGender());
            employeeDB.setJobTitle(employee.getJobTitle());
            employeeDB.setState(employee.getState());
            employeeDB.setCity(employee.getCity());
            employeeDB.setConsumer(employee.getConsumer());
            employeesDB.add(employeeDB);

        }
        return employeesDB;
    }
}
