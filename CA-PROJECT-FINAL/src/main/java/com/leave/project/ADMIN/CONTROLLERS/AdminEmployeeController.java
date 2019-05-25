package com.leave.project.ADMIN.CONTROLLERS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.BEANS.UserSession;
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
	@Autowired
	public void setRoleRepo(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}
	/*
	  	USE -----HttpSession session AS CONTROLLER METHOD PARAMETER    ----- FOR ANY CONTROLLER 
	  	UserSession temp= (UserSession)session.getAttribute("USER");
	  	model.addAttribute("ERROR","UN-Authorized Access");
	  	if(temp==null)
				return "redirect:/*";   ///default path for login
	 
		Employee t=temp.getEmployee();

		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {    //FOR EACH CONTROLLER CERTAIN ROLES WILL BE THERE
			
			return "redirect:/*";
		} 
		
		AND USER LOGIN WILL WORK AS ALWAYS  ------ FOR TESTING PURPOSE PUT USERNAME :   admin    PASSWORD :   admin 
		
		THIS IS JUST FOR SAYING WE USED SPRING SECURITY ....WHAT WE DID IS SESSION BASED SECURITY
		
	 	THIS IS THE CODE FOR VALIDATING THE USER SESSION
	 */
	
	@GetMapping(path = "/admin/Employee/view")
	public String getEmployeeList(Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		ArrayList<Employee> list = (ArrayList<Employee>) empRepo.findAll();
		list = (ArrayList<Employee>)list.stream().filter(emp -> !emp.getRole().getRoleName().equalsIgnoreCase("admin")).collect(Collectors.toList());
		model.addAttribute("employees", list);
		return "ViewEmployees";
	}
	
	
	@GetMapping(path = "/admin/Employee/add")
	public String getEmployeeForm(Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		List<Employee> managers = empRepo.findAll();
		managers = (List<Employee>)managers.stream().filter(emp -> emp.getRole().getRoleName().equalsIgnoreCase("manager")).collect(Collectors.toList());
		model.addAttribute("MANAGERS", managers);
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAll());
		model.addAttribute("employee", new Employee());
		return "EmployeeAdd";
	}
	
	
	
	
	@PostMapping(path = "/admin/Employee")
	public String saveEmployee(Model model,Employee E,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		try{
			empRepo.save(E);
		}catch(Exception e) {
			System.out.println("Error Bro What To do");
		}
		return "redirect:/admin/Employee/view";
	}
	
	
	@GetMapping(path = "/admin/Employee/edit/{id}")
	public String editProduct(@PathVariable(value = "id") int id,Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		List<Employee> managers = empRepo.findAll();
		managers = (List<Employee>)managers.stream().filter(emp -> emp.getRole().getRoleName().equalsIgnoreCase("manager")).collect(Collectors.toList());
		model.addAttribute("MANAGERS", managers);
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAll());
		model.addAttribute("employee", empRepo.findById(id));
		return "EmployeeAdd";
	}
	
	
	
	@RequestMapping(path = "/admin/Employee/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		try{
			empRepo.delete(empRepo.findById(id).orElse(null));
		}catch(Exception e) {
			System.out.println("Employee Referencing himself");
		}
		return "redirect:/admin/Employee/view";
	}
	
}
