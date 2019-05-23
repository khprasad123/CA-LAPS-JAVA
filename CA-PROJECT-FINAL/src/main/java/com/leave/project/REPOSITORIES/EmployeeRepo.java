package com.leave.project.REPOSITORIES;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.Role;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
	@Query(nativeQuery = true, value="SELECT * from Employee E where E.Role_Id in (select role_id from Role where role_name='Manager')")
	public ArrayList<Employee> findAllManagers();
}
