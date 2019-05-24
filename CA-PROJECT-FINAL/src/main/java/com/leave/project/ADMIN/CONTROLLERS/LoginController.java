package com.leave.project.ADMIN.CONTROLLERS;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.leave.project.BEANS.UserSession;
import com.leave.project.FORMS.LoginForm;
import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
@SessionAttributes("session")
public class LoginController {
	// EVERY EMPLOYEE SHOULD LOGIN TO PROCEED FURTHER
	@Autowired
	private EmployeeRepo empRepo;
	@Autowired
	private IEmployeeService emp;

	@GetMapping(path = "/*")
	public ModelAndView justLogin() {
		ModelAndView mv = new ModelAndView("Login");
		mv.addObject("employee", new LoginForm());
		return mv;
	}

	@PostMapping(path = "/*/authenticate")
	public ModelAndView onAuthen(@ModelAttribute LoginForm login, HttpServletRequest hiddenUser,HttpSession session) {

		ModelAndView mv = new ModelAndView("Login");
		login.error = "Login Failed ---Invalid Credentials ";
		mv.addObject("employee", login);
		
		Employee E = emp.authenticate(login.getUsername(), login.getPassword());
		if (E == null)
			return mv;
		
		switch (E.getRole().getRoleName()) {
		
		case "Admin":
			mv = new ModelAndView("ADMIN");
			break;
		case "Staff":
			mv = new ModelAndView("STAFF");
			break;
		case "Manager":
			mv = new ModelAndView("MANAGER");
			break;
		}
		UserSession user = new UserSession();
		user.setEmployee(E);
		// setting the user //// ------SO EVERY USER MUST LOGIN TO USE ANY FUNCTIONS
		hiddenUser.getSession().setAttribute("USER", user);
		// for Setting User
		UserSession temp = (UserSession) session.getAttribute("USER");
		/// for getting user

		return mv;
	}

	// WHEN LOGGING OUT THE USER WILL GET LOSTED
	@GetMapping(path = "/*/logout")
	public String justLogout(Model model, HttpServletRequest hiddenUser, HttpSession session) {
		hiddenUser.getSession().setAttribute("USER", null);
		return "redirect:/";
	}

}
