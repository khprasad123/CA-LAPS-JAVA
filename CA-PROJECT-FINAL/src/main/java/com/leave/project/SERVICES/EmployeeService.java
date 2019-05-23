package com.leave.project.SERVICES;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;

@Service
public class EmployeeService implements IEmployeeService {
	@Autowired
	private EmployeeRepo empRepo;

	@Transactional
	public Employee authenticate(String userName,String password) {
		System.out.println(userName + password);
		System.out.println("I am inside the service Class");
		List<Employee> temp=(List<Employee>)empRepo.findByUserNameAndPassword(userName, password);
		if(temp==null)
			return null;
		return temp.get(0);
	}
	
	
}
