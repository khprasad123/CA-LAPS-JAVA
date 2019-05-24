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
import com.leave.project.MODELS.LeaveType;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
@Controller
public class AdminLeaveController {
	@Autowired
	private LeaveTypeRepo leaveRepo;

	@GetMapping(path="/admin/LeaveType/view")
	public String viewHollydays(Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		model.addAttribute("leavetypes",leaveRepo.findAll());
		return "ViewLeaveTypes";
	}
	@GetMapping(path="/admin/LeaveType/add")
	public String getHollydayForm(Model model,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		model.addAttribute("leavetype",new LeaveType());
		return "LeaveTypeForm";
	}
	@PostMapping(path="/admin/LeaveType")
	public String saveLeaveType(Model model,LeaveType E,HttpSession session) {
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		
		leaveRepo.save(E);
		return "redirect:/LeaveType/view";
	}

	 @GetMapping(path="*/admin/LeaveType/edit/{id}") 
	 public String editHollyday(@PathVariable(name = "id") int id,Model model,HttpSession session) {
			
			UserSession temp= (UserSession)session.getAttribute("USER");
			model.addAttribute("ERROR","UN-Authorized Access");
			if(temp==null)
					return "redirect:/*/logout";
			Employee t=temp.getEmployee();
			if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
				return "redirect:/*/logout";     ///defaulf path for logging out safely
			}
			
			
		 model.addAttribute("leavetype",leaveRepo.findById(id)); 
		 return "LeaveTypeForm";
	 }
	 
	@RequestMapping(path ="/admin/LeaveType/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model,HttpSession session){
		
		UserSession temp= (UserSession)session.getAttribute("USER");
		model.addAttribute("ERROR","UN-Authorized Access");
		if(temp==null)
				return "redirect:/*/logout";
		Employee t=temp.getEmployee();
		if(t == null|| !(t.getRole().getRoleName().equals("Admin"))) {
			return "redirect:/*/logout";     ///defaulf path for logging out safely
		}
		
		leaveRepo.delete(leaveRepo.findById(id).orElse(null)); 
		return "redirect:/LeaveType/view";
	}
}
