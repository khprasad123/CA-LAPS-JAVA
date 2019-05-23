package com.leave.project.CONTROLLERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.RoleRepo;

@Controller
public class AdminEmployeeController {
	private EmployeeRepo empRepo;
	@Autowired
	public void setEmprepo(EmployeeRepo emprepo) {
		this.empRepo = emprepo;
	}
	
	private RoleRepo roleRepo;
	
	//All the Controlls for Employee Operations
	@Autowired
	public void setRoleRepo(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	
	@GetMapping(path = "/Employee/view")
	public String getEmployeeList(Model model) {
		ArrayList<Employee> list = (ArrayList<Employee>) empRepo.findAll();
		list = (ArrayList<Employee>)list.stream().filter(emp -> !emp.getRole().getRoleName().equalsIgnoreCase("admin")).collect(Collectors.toList());
		model.addAttribute("employees", list);
	//trying to do something
		return "ViewEmployees";
	}
	@GetMapping(path = "/Employee/add")
	public String getEmployeeForm(Model model) {
		List<Employee> managers = empRepo.findAllManagers();
		model.addAttribute("MANAGERS", managers);
		System.out.println("hey i am here");
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAllRoles());
		System.out.println("hey i am in the middle");
		model.addAttribute("employee", new Employee());
		return "EmployeeAdd";
	}
	@PostMapping(path = "/Employee")
	public String saveEmployee(Model model,Employee E) {
		System.out.println("Iam in side the controller bro");
		try{
			empRepo.save(E);
		}catch(Exception e) {
			System.out.println("Error Bro What To do");
		}
		return "redirect:/Employee/view";
	}
	
	@RequestMapping(path = "/Employee/edit/{id}", method = RequestMethod.GET)
	public String editProduct(Model model, @PathVariable(value = "id") int id) {
		List<Employee> managers = empRepo.findAllManagers();
		model.addAttribute("MANAGERS", managers);
		Employee E = empRepo.findById(id).orElse(null);
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAllRoles()); 
		model.addAttribute("EMPLOYEE_RT",E.getReportsTo().getEmpId()); //just for checking
		System.out.println("hey i am in the middle");
		model.addAttribute("employee", E);
		
		model.addAttribute("MANAGERS", managers);
		return "EmployeeEdit";
	}
//for deletion simple 
	@RequestMapping(path = "/Employee/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id) {
		try{
			empRepo.delete(empRepo.findById(id).orElse(null));
		}catch(Exception e) {
			System.out.println("Employee Referencing himself");
		}
		return "redirect:/Employee/view";
	}
	
}
