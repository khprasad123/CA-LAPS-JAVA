package com.leave.project.LEAVE.CONTROLLERS;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.LeaveHistoryDetails;
import com.leave.project.MODELS.LeaveType;
import com.leave.project.MODELS.Role;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.LeaveHistoryRepo;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
import com.leave.project.SERVICES.LeaveAppService;
import com.leave.project.UTILITIES.Status;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Controller
public class LeaveAppController {
	
	@Autowired
	private LeaveTypeRepo leaveTypeRepo;
	
	@Autowired
	private LeaveHistoryRepo leaveHistoryDetailsRepo;
	
	@Autowired
    private EmployeeRepo employeeRepo;

	 private List<LeaveHistoryDetails> leaveList;
	 private List<LeaveHistoryDetails> toExportList = new ArrayList<LeaveHistoryDetails>();
	 private String intro = "I'm sorry to inform that I can't accept your request. Because ";
	
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
	
	@RequestMapping(path="/Employee/leaveAppForm",method=RequestMethod.POST)
	public String leaveAppSubmit(@Valid LeaveHistoryDetails leaveDetails,BindingResult result,Model model)
	{
		
		if(LeaveAppService.compareDate(leaveDetails))
		{
			leaveDetails.setEmployee(employeeRepo.findById(2).get());
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
	
	
	  @RequestMapping(path = "/leave/approval_list", method = RequestMethod.GET)
	    public String showEmployeeList(Model model) {
	    	Role role = new Role(1,"manager");
	       	Employee emp = new Employee (2,"hari@email.com","Hari","hari","hari",role);

	    	List<Employee> empList = employeeRepo.findByReportsTo(emp);
	    	leaveList = new ArrayList<LeaveHistoryDetails>();
	    	List<LeaveHistoryDetails> temp = new ArrayList<LeaveHistoryDetails>();
	    	
	    	Iterator<Employee> i = empList.iterator();
			while (i.hasNext()) {
				temp = leaveHistoryDetailsRepo.findByEmployeeAndStatus(i.next(),Status.APPLIED );
				if(temp.size() != 0)leaveList.addAll(temp);
			}
			
	        model.addAttribute("leave_list",leaveList);

	        return "approval_list";
	    }
	  
	  @RequestMapping(path = "/leave/leave_history", method = RequestMethod.GET)
	    public String showLeaveHistory(Model model) {
	    	List<Employee> empList = getEmployeeList();
	    	leaveList = new ArrayList<LeaveHistoryDetails>();
	    	
	    	Iterator<Employee> i = empList.iterator();
			while (i.hasNext()) {
				leaveList.addAll( leaveHistoryDetailsRepo.findByEmployee(i.next()));
			}
			toExportList = leaveList;
	        model.addAttribute("status","ALL");
	        model.addAttribute("leave_type","ALL");
	        model.addAttribute("leave_list",leaveList);
	        model.addAttribute("leave_type_lists",leaveTypeRepo.findAll());

	        return "leave_history";
	    }
	  	private List<Employee> getEmployeeList(){
	    	Role role = new Role(1,"manager");
	    	//    	Employee emp = new Employee (5,"smith@gmail.com","Smith","smith","smith",role);
	    	Employee emp = new Employee (2);
	    	List<Employee> empList = employeeRepo.findByReportsTo(emp);
			return empList;
	    }
	    
	  	@RequestMapping(path = "/leave/filter", method = RequestMethod.GET)
	    public String filterLeaveHistory(@RequestParam String leave_type,@RequestParam String status,@RequestParam String start_date,@RequestParam String end_date,Model model) {
	    	boolean dateFilter = false;
	    	toExportList.clear();
	    	if(start_date != "" && end_date != "") {
	    	dateFilter = true;
	    	toExportList = leaveList.stream().filter(leave -> leave.check(start_date,end_date)).collect(Collectors.toList());
	    	}
	    	
	    	if(leave_type.equals("ALL") && status.equals("ALL")) {
	    		if(!dateFilter) {
	    			return "redirect:/leave/leave_history";
	    		}
	    	}else if(!leave_type.equals("ALL") && !status.equals("ALL")) {
	    		toExportList = leaveList.stream().filter(leave -> (leave.getLeaveType().getType().equals(leave_type) && leave.getStatus().equals(status))).collect(Collectors.toList());
	    	}else if(status.equals("ALL")) {
	    		toExportList = leaveList.stream().filter(leave -> leave.getLeaveType().getType().equals(leave_type)).collect(Collectors.toList());
	    	}else if(leave_type.equals("ALL") ) {
	    		toExportList = leaveList.stream().filter(leave -> leave.getStatus().equals(status)).collect(Collectors.toList());
	    	}
	        model.addAttribute("status",status); 
	        model.addAttribute("leave_type",leave_type);
	    	model.addAttribute("leave_list",toExportList);
	        model.addAttribute("leave_type_lists",leaveTypeRepo.findAll());

	    	return "leave_history";
	    }
	    
	    
	  //Export  to CSV file
	    @GetMapping("/leave/export")
	    public void exportCSV(HttpServletResponse response) throws Exception {
	        //set file name and content type
	        String filename = "LeaveList.csv";

	        response.setContentType("text/csv");
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + filename + "\"");
	        	
	        //Header
	        PrintWriter pw = response.getWriter().append("NAME,LEAVE_TYPE,START_DATE,END_DATE,APPLY_REASON,REJECT_REASON,WORK_DESEMINATION\n");

	        //create a csv writer
	        StatefulBeanToCsv<LeaveHistoryDetails> writer = new StatefulBeanToCsvBuilder<LeaveHistoryDetails>(pw)
	                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) 
	                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
	                .withOrderedResults(false)
	                .build();

	        // leaveRepo.findAll(new Sort(Sort.Direction.DESC, "id"));
	               
	        writer.write(toExportList);
	        }
	    
	    @RequestMapping(path = "leave/edit_leave/{leave_history_id}", method = RequestMethod.GET)
		public String updateleave(Model model,@PathVariable(value = "leave_history_id") String leave_history_id) {
			LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
			model.addAttribute("updateleave", lhd); 
			return "edit_leave";
		}
	    
	    @RequestMapping(path = "leave/leave_detail/{leave_history_id}", method = RequestMethod.GET)
	   	public String leaveDetail(Model model,@PathVariable(value = "leave_history_id") String leave_history_id) {
	   		LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
	   		model.addAttribute("leave_detail", lhd); 
	   		return "leave_detail";
	   	}
	    
		@RequestMapping(path = "leave/edit_leave/{leave_id}", method=RequestMethod.POST)
		public String saveleavestatus(String action,@PathVariable(value = "leave_id") String leave_history_id,@RequestParam("reasons") String reasons ) {
			LeaveHistoryDetails ldh = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
			Employee emp = ldh.getEmployee();
			String leave_type =ldh.getLeaveType().getType();
			
			System.out.println("*******************");
	    	System.out.println("Size "+emp.getEmail());

			if(action.equals("2"))
			{
				ldh.setStatus(Status.REJECTED);
				ldh.setRejectionReason(reasons);
				sendMail(emp.getEmail(),Status.REJECTED.getStatus(),intro+reasons);
			}
			else if (action.equals("1"))
			{
				int count = ldh.getLeaveCount();
				if(leave_type.equals("Annual Leave")) {
					emp.setAnnualLeaveCount(emp.getAnnualLeaveCount()-count);
				}else if(leave_type.equals("Medical Leave")) {
					emp.setMedicalLeaveCount(emp.getMedicalLeaveCount()-count);
				}else {
					emp.setCompensationLeaveCount(emp.getCompensationLeaveCount()-count);
				}
				ldh.setStatus(Status.APPROVED);
				 sendMail(emp.getEmail(),Status.APPROVED.getStatus(),"Ok.I accept.");

				employeeRepo.save(emp);
			}
			//else 
			leaveHistoryDetailsRepo.save(ldh);
			return "redirect:/leave/approval_list";
		}
	
//		********* Send Mail *********
		@Autowired
	    private JavaMailSender javaMailSender;
		public  void sendMail(String email, String status,String reasons) {
			 SimpleMailMessage msg = new SimpleMailMessage();
	         msg.setTo(email);
	         msg.setFrom("hninnwe.coder@gmail.com");

	         msg.setSubject("LEAVE "+status);
	         msg.setText(reasons);

	         javaMailSender.send(msg);
		}
		
	}

