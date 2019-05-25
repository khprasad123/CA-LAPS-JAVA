package com.leave.project.SECURITY;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	  @GetMapping("/") 
	  public String home() {
		  return "Login"; 
	  }
	  
	  @GetMapping("/login")
	  public String loginPage() { 
		  return "Login"; 
	  
	  }
	  
	  @GetMapping("/logout") public String logoutPage() { 
		  return "Login"; 
	  }
	 
}
