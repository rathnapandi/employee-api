package com.axway.pagination.api;

import com.axway.pagination.mapper.EmployeeMapper;
import com.axway.pagination.model.Employee;
import com.axway.pagination.model.EmployeeDTO;
import com.axway.pagination.repo.EmployeeRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final CsvMapper mapper = new CsvMapper();

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    public EmployeeController(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }


    @GetMapping
    public Page<Employee> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.findAll(pageRequest);
    }

    @GetMapping("/latest")
    public Page<Employee> findAllLatestByCreatedDate(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam("dateTime") String date) {
        logger.info("Date : {}", date);
        PageRequest pageRequest = PageRequest.of(page, size);
        return employeeRepository.findByCreatedAtGreaterThanEqual(pageRequest, Timestamp.from(Instant.parse(date)));
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/{id}")
    public Employee update(@RequestBody EmployeeDTO employee, @PathVariable("id") long id) {
        Optional<Employee> value = employeeRepository.findById(id);
        if (value.isPresent()) {
            Employee existingEmployee = value.get();
            employeeMapper.updateEmployee(employee, existingEmployee);
            return employeeRepository.save(existingEmployee);
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        );
    }


    @PostMapping("/bulk")
    public void handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        try (InputStream inputStream = file.getInputStream()) {
            MappingIterator<com.axway.pagination.bulk.Employee> mappingIterator = mapper.readerWithTypedSchemaFor(com.axway.pagination.bulk.Employee.class).with(CsvSchema.emptySchema().withHeader()).readValues(inputStream);
            List<com.axway.pagination.bulk.Employee> employees = mappingIterator.readAll();
            List<Employee> employeesDB = getEmployees(employees);
            Iterable<Employee> savedData = employeeRepository.saveAll(employeesDB);
            long count = StreamSupport.stream(savedData.spliterator(), false).count();
            logger.info("Numbers of records updated : {}", count);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable("id") long id) {
        employeeRepository.deleteById(id);
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        employeeRepository.deleteAll();
    }


    private static List<Employee> getEmployees(List<com.axway.pagination.bulk.Employee> employees) {
        List<Employee> employeesDB = new ArrayList<>();
        for (com.axway.pagination.bulk.Employee employee : employees) {
            Employee employeeDB = new Employee();
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
