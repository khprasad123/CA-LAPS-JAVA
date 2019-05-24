package com.leave.project.ADMIN.CONTROLLERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.LeaveType;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
@Controller
public class AdminLeaveController {
	@Autowired
	private LeaveTypeRepo leaveRepo;

	@GetMapping(path="/LeaveType/view")
	public String viewHollydays(Model model) {
		model.addAttribute("leavetypes",leaveRepo.findAll());
		return "ViewLeaveTypes";
	}
	@GetMapping(path="/LeaveType/add")
	public String getHollydayForm(Model model) {
		model.addAttribute("leavetype",new LeaveType());
		return "LeaveTypeForm";
	}
	@PostMapping(path="/LeaveType")
	public String saveLeaveType(Model model,LeaveType E) {
		leaveRepo.save(E);
		return "redirect:/LeaveType/view";
	}

	 @GetMapping(path="/LeaveType/edit/{id}") 
	 public String editHollyday(@PathVariable(name = "id") int id,Model model) {
		 model.addAttribute("leavetype",leaveRepo.findById(id)); 
		 return "LeaveTypeForm";
	 }
	 
	@RequestMapping(path ="/LeaveType/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model){
		leaveRepo.delete(leaveRepo.findById(id).orElse(null)); 
		return "redirect:/leaveRepo/view";
	}
}
