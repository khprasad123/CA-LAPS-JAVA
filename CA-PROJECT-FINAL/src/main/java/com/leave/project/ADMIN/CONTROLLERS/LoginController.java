package com.leave.project.ADMIN.CONTROLLERS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.leave.project.BEANS.UserSession;
import com.leave.project.MODELS.Employee;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
@SessionAttributes("session")
public class LoginController {
	@Autowired
	private IEmployeeService emp;
	
	
	@PostMapping(path = "/*/authenticate")
	public String onAuthen(HttpServletRequest hiddenUser,HttpSession session,Model model) {		
		String mv="";
		Employee E =emp.GetUser();
		mv=E.getRole().getRoleName().toUpperCase();  //Each view is Get for a particular role  if(admin   then view is ADMIN  

		UserSession user = new UserSession();
		user.setEmployee(E); 
		hiddenUser.getSession().setAttribute("USER", user);
		
		return mv;
	}
}
