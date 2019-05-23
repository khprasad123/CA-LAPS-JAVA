package com.leave.project.CONTROLLERS;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.leave.project.BEANS.UserSession;
import com.leave.project.FORMS.LoginForm;
import com.leave.project.MODELS.Employee;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
@SessionAttributes("session")
public class SimpleControler {
		
		@Autowired
		private IEmployeeService emp;
		
		@GetMapping(path="/Login")
		public ModelAndView justLogin() {
			ModelAndView mv=new ModelAndView("Login");
			mv.addObject("employee",new LoginForm());
			return mv;
		}
		@PostMapping(path="/authenticate")
		public ModelAndView onAuthentication(@ModelAttribute LoginForm login,HttpSession session) {
			ModelAndView mv=new ModelAndView("Login");
			login.error="Login Failed ---Invalid Credentials ";
			mv.addObject("employee",login);
			if(login==null || login.getUsername().equals("")||login.getPassword().equals("") )
				return mv;
			
			System.out.println(login.getUsername());System.out.println(login.getPassword());
			
			Employee E =emp.authenticate(login.getUsername(),login.getPassword());
			if(E==null)
					return mv;
			
			switch(E.getRole().getRoleName()) {
			case "Admin":mv=new ModelAndView("ADMIN");
						 break;
				
			case "Staff":mv=new ModelAndView("STAFF");
			 			 break;
			case "Manager":mv=new ModelAndView("MANAGER");
			 			 break;
				
			}
			UserSession user=new UserSession();
			user.setEmployee(E);
			session.setAttribute("USER", user);
			
			return mv;
		}
	
}
