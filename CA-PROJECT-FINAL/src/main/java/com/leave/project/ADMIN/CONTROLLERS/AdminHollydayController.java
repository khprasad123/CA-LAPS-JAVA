package com.leave.project.ADMIN.CONTROLLERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.PublicHollyday;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;

@Controller
public class AdminHollydayController {
	private PublicHollydayRepo phRepo;
	@Autowired
	public void setPhRepo(PublicHollydayRepo phRepo) {
		this.phRepo = phRepo;
	}
	
	@GetMapping(path="/Hollyday/view")
	public String viewHollydays(Model model) {
		model.addAttribute("hollydays",phRepo.findAll());
		return "ViewHollydays";
	}
	@GetMapping(path="/Hollyday/add")
	public String getHollydayForm(Model model) {
		model.addAttribute("hollyday",new PublicHollyday());
		return "Hollydays";
	}
	@PostMapping(path="/Hollyday")
	public String saveHollyday(Model model,PublicHollyday E) {
		phRepo.save(E);
		return "redirect:/Hollyday/view";
	}

	
	 @GetMapping(path="/Hollyday/edit/{id}") 
	 public String editHollyday(@PathVariable(name = "id") int id,Model model) {
		 model.addAttribute("hollyday",phRepo.findById(id)); 
		 return "Hollydays";
	 }
	 
	
//for deletion simple 
	@RequestMapping(path ="/Hollyday/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model){
		phRepo.delete(phRepo.findById(id).orElse(null)); 
		return "redirect:/Hollyday/view";
	}
}
