package com.leave.project.ADMIN.CONTROLLERS;

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
import com.leave.project.MODELS.PublicHollyday;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;

@Controller
public class AdminHollydayController {
	private PublicHollydayRepo phRepo;
	@Autowired
	public void setPhRepo(PublicHollydayRepo phRepo) {
		this.phRepo = phRepo;
	}
	
	@GetMapping(path="/admin/Hollyday/view")
	public String viewHollydays(Model model,HttpSession session ) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		model.addAttribute("hollydays",phRepo.findAll());
		return "ViewHollydays";
	}
	@GetMapping(path="/admin/Hollyday/add")
	public String getHollydayForm(Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		
		model.addAttribute("hollyday",new PublicHollyday());
		return "Hollydays";
	}
	@PostMapping(path="/admin/Hollyday")
	public String saveHollyday(Model model,PublicHollyday E,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		phRepo.save(E);
		return "redirect:/admin/Hollyday/view";
	}

	
	 @GetMapping(path="/admin/Hollyday/edit/{id}") 
	 public String editHollyday(@PathVariable(name = "id") int id,Model model,HttpSession session) {
			
			UserSession temp= (UserSession)session.getAttribute("USER");
			model.addAttribute("ERROR","UN-Authorized Access");
			if(temp==null)
					return "redirect:/*/logout";
			Employee t=temp.getEmployee();
			if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
				return "redirect:/*/logout";     ///defaulf path for logging out safely
			}
		 
		 model.addAttribute("hollyday",phRepo.findById(id)); 
		 return "Hollydays";
	 }
	 
	
//for deletion simple 
	@RequestMapping(path ="/admin/Hollyday/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model,HttpSession session){
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		
		phRepo.delete(phRepo.findById(id).orElse(null)); 
		return "redirect:/Hollyday/view";
	}
}
