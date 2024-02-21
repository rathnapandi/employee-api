package com.axway.pagination.repo;

import com.axway.pagination.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {


    Page<Employee> findAll( Pageable pageable);
   // @Query("Select e from Employee e where e.createdAt <= ?1")
  // @Query(value = "select e from Employee e where e.createdAt >= CAST (:creationDateTime as timestamp without time zone)", nativeQuery = true)

    //@Query("Select e from Employee e where e.createdAt <= cast(timestamp(:createdAt))")
   Page<Employee> findByCreatedAtGreaterThanEqual(Pageable page, Timestamp date);
}
