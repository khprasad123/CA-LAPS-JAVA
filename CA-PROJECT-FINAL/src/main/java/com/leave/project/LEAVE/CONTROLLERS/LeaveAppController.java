package com.leave.project.LEAVE.CONTROLLERS;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.LeaveHistoryDetails;
import com.leave.project.MODELS.LeaveType;
import com.leave.project.REPOSITORIES.LeaveHistoryRepo;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
import com.leave.project.SERVICES.LeaveAppService;
import com.leave.project.UTILITIES.Status;

@Controller
public class LeaveAppController {
	@Autowired
	private LeaveTypeRepo leaveTypeRepo;
	
	@Autowired
	private LeaveHistoryRepo leaveHistoryDetailsRepo;
	
	@RequestMapping(path="/staffView",method=RequestMethod.GET)
	public String staffView()
	{
		return "StaffView";
	}
	
	@RequestMapping(path="/leaveAppForm",method=RequestMethod.GET)
	public String leaveAppForm(Model model)
	{
		model.addAttribute("leaveDetails", new LeaveHistoryDetails());
		List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}
	
	@RequestMapping(path="/leaveAppForm",method=RequestMethod.POST)
	public String leaveAppSubmit(@Valid LeaveHistoryDetails leaveDetails,BindingResult result,Model model)
	{
		
		if(LeaveAppService.compareDate(leaveDetails))
		{
			leaveHistoryDetailsRepo.save(leaveDetails);
			List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatusOrStatus(Status.APPLIED,Status.UPDATED);
			model.addAttribute("leaveHistoryList", leaveHistoryList);
			return "ManageLeaveApp";
		}
		else
		{
			model.addAttribute("error", "error");
			model.addAttribute("message", "End date cannot be less than Start Date");
			model.addAttribute("leaveDetails", leaveDetails);
			List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
			model.addAttribute("leaveTypeList", leaveTypeList);
			return "LeaveAppForm";
		}
		
	}
	
	@RequestMapping(path="/manageLeaveDetails",method=RequestMethod.GET)
	public String manageLeaveApp(LeaveHistoryDetails leaveDetails,Model model)
	{
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatusOrStatus(Status.APPLIED,Status.UPDATED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ManageLeaveApp";
	}
	

	@RequestMapping(path="/updateLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String updateLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.UPDATED);
		model.addAttribute("leaveDetails",leavehistory);
		List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}
	

	@RequestMapping(path="/deleteLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String deleteLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.DELETED);
		leaveHistoryDetailsRepo.save(leavehistory);
		return "redirect:/manageLeaveDetails";
	}
	
	@RequestMapping(path="/viewApprovedLeaves",method=RequestMethod.GET)
	public String viewApprovedLeaves(LeaveHistoryDetails leaveDetails,Model model)
	{
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}
	
	@RequestMapping(path="/cancelLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String cancelLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.CANCELLED);
		leaveHistoryDetailsRepo.save(leavehistory);
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}
	
	
	@RequestMapping(path="/viewPersonalLeaveHistory",method=RequestMethod.GET)
	public String viewPersonalLeaveHistory(LeaveHistoryDetails leaveDetails,Model model)
	{
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findAll();
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewPersonalLeaveHistory";
		
	}
}
