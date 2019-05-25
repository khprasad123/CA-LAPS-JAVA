package com.leave.project.LEAVE.CONTROLLERS;

import java.io.PrintWriter;
import java.sql.Date;
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
import com.leave.project.MODELS.PublicHollyday;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.LeaveHistoryRepo;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;
import com.leave.project.SERVICES.IEmployeeService;
import com.leave.project.SERVICES.LeaveAppService;
import com.leave.project.UTILITIES.Status;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Controller
public class LeaveAppController {
	
	@Autowired
	private IEmployeeService emp;
	
	@Autowired
	private LeaveTypeRepo leaveTypeRepo;
	
	@Autowired
	private LeaveHistoryRepo leaveHistoryDetailsRepo;
	
	@Autowired
    private EmployeeRepo employeeRepo;

	@Autowired
	private PublicHollydayRepo publicrepo;
	
	 private List<LeaveHistoryDetails> leaveList;
	 private List<LeaveHistoryDetails> toExportList = new ArrayList<LeaveHistoryDetails>();
	 private String intro = "I'm sorry to inform that I can't accept your request. Because ";
	
	@RequestMapping(path="/staff/staffView",method=RequestMethod.GET)
	public String staffView()
	{
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		return "STAFF";
	}
	
	@RequestMapping(path="/staff/leaveAppForm",method=RequestMethod.GET)
	public String leaveAppForm(Model model)
	{
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		model.addAttribute("leaveDetails", new LeaveHistoryDetails());
		List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}
	

	@RequestMapping(path="/staff/leaveAppForm",method=RequestMethod.POST)
	public String leaveAppSubmit(@Valid LeaveHistoryDetails leaveDetails,BindingResult result,Model model)
	{
		
		Employee t=emp.GetUser();
		
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		leaveDetails.setEmployee(t);
		List<PublicHollyday> list = new ArrayList<PublicHollyday>();
		list = publicrepo.findByStartDateOrStartDate(leaveDetails.getStartDate(),leaveDetails.getEndDate());
		int numberofHoliday =  list.size();
		
		List<LeaveHistoryDetails> leaveHistoryList1 = leaveHistoryDetailsRepo.findByStatusOrStatusNot(Status.DELETED,Status.CANCELLED);
		
		leaveHistoryList1 = leaveHistoryList1.stream().filter(leaveAlready->leaveAlready.check(leaveDetails.getStartDate(),leaveDetails.getEndDate())).collect(Collectors.toList());
		int compareResult = LeaveAppService.compareDate(leaveDetails,numberofHoliday);
		List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
		model.addAttribute("leaveTypeList", leaveTypeList);
		model.addAttribute("leaveDetails", leaveDetails);
	
		if(compareResult == LeaveAppService.SUCCESS)
		{

			if(leaveHistoryList1.size() == 0)
			{
				list = publicrepo.findByStartDateBetween(leaveDetails.getStartDate(),leaveDetails.getEndDate());
				if(LeaveAppService.numberOfDays(leaveDetails,numberofHoliday))
				{
					leaveHistoryDetailsRepo.save(leaveDetails);
					List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatusOrStatus(Status.APPLIED,Status.UPDATED);
					model.addAttribute("leaveHistoryList", leaveHistoryList);
					return "ManageLeaveApp";
				}
				else
				{
					model.addAttribute("error", "error");
					model.addAttribute("message", "Your" + " "+leaveDetails.getLeaveType().getType() + " " + "exceeded the Leave Balance Limit");
					return "LeaveAppForm";
				}
				
			}
			
			else
			{
				model.addAttribute("error", "error");
				model.addAttribute("message", "You can't apply leave for already applied leave dates.");
				return "LeaveAppForm";
			}
			
		 }
		else
		{
			String message ="";
			if(compareResult == LeaveAppService.ENDDATE_LESS_THAN_ERROR ) {
				message = "End date cannot be less than Start Date.";
			}else {
				message = "Start and End-date must be working days.";
			}

			model.addAttribute("message", message);
			model.addAttribute("error", "error");

			return "LeaveAppForm";
		}
		
	
		
	}
	
	@RequestMapping(path="/staff/manageLeaveDetails",method=RequestMethod.GET)
	public String manageLeaveApp(LeaveHistoryDetails leaveDetails,Model model)
	{
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatusOrStatus(Status.APPLIED,Status.UPDATED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ManageLeaveApp";
	}
	

	@RequestMapping(path="/staff/updateLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String updateLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.UPDATED);
		model.addAttribute("leaveDetails",leavehistory);
		List<LeaveType> leaveTypeList = leaveTypeRepo.findAll();
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}
	

	@RequestMapping(path="/staff/deleteLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String deleteLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.DELETED);
		leaveHistoryDetailsRepo.save(leavehistory);
		return "redirect:/staff/manageLeaveDetails";
	}
	
	@RequestMapping(path="/staff/viewApprovedLeaves",method=RequestMethod.GET)
	public String viewApprovedLeaves(LeaveHistoryDetails leaveDetails,Model model)
	{
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}
	
	@RequestMapping(path="/staff/cancelLeaveForm/{leaveHistoryId}",method=RequestMethod.GET)
	public String cancelLeaveForm(@PathVariable(value="leaveHistoryId") int leaveHistoryId,Model model)
	{  
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.CANCELLED);
		leaveHistoryDetailsRepo.save(leavehistory);
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}
	
	
	@RequestMapping(path="/staff/viewPersonalLeaveHistory",method=RequestMethod.GET)
	public String viewPersonalLeaveHistory(LeaveHistoryDetails leaveDetails,Model model)
	{
		Employee t=emp.GetUser();
		if(t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findAll();
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewPersonalLeaveHistory";
		
	}
	
//****************************************** Manager's part ************************************************************
	  @RequestMapping(path = "/leave/approval_list", method = RequestMethod.GET)
	    public String showEmployeeList(Model model) {
	        
			Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
	    	
	    	
	    	List<Employee> empList = employeeRepo.findByReportsTo(t);
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
			
		  	Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
		  
		  
		  	List<Employee> empList = getEmployeeList();
	    	leaveList = new ArrayList<LeaveHistoryDetails>();
	    	
	    	Iterator<Employee> i = empList.iterator();
			while (i.hasNext()) {
				leaveList.addAll( leaveHistoryDetailsRepo.findByEmployee(i.next()));
			}
			toExportList.addAll(leaveList);
			model.addAttribute("startdate","dd/mm/yyyy");
	    	model.addAttribute("enddate","dd/mm/yyyy");
	        model.addAttribute("status","ALL");
	        model.addAttribute("leave_type","ALL");
	        model.addAttribute("leave_list",leaveList);
	        model.addAttribute("leave_type_lists",leaveTypeRepo.findAll());

	        return "leave_history";
	    }
	  	private List<Employee> getEmployeeList(){
	  		Employee E=emp.GetUser();
	    	List<Employee> empList = employeeRepo.findByReportsTo(E);
			return empList;
	    }
	    
	  	@RequestMapping(path = "/leave/filter", method = RequestMethod.GET)
	    public String filterLeaveHistory(@RequestParam String leave_type,@RequestParam String status,@RequestParam String start_date,@RequestParam String end_date,Model model) {
	  		Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
	  		
	  		
	  		boolean dateFilter = false;
	    	toExportList.clear();
	    	if(start_date != "" && end_date != "") {
	    	dateFilter = true;
	    	toExportList = leaveList.stream().filter(leave -> leave.check(Date.valueOf(start_date),Date.valueOf(end_date))).collect(Collectors.toList());
	    	}
	    	
	    	
			System.out.println("*******************");
	    	System.out.println("Size "+leaveList.size());

	    	if(leave_type.equals("ALL") && status.equals("ALL")) {
	    		if(!dateFilter) {
	    			return "redirect:/leave/leave_history";
	    		}
	    	}else if(!leave_type.equals("ALL") && !status.equals("ALL")) {
	    		toExportList = leaveList.stream().filter(leave -> (leave.getLeaveType().getType().equals(leave_type) && leave.getStatus().get().equals(status))).collect(Collectors.toList());
	    	}else if(status.equals("ALL")) {
	    		toExportList = leaveList.stream().filter(leave -> leave.getLeaveType().getType().equals(leave_type)).collect(Collectors.toList());
	    	}else if(leave_type.equals("ALL") ) {
	    		toExportList = leaveList.stream().filter(leave -> leave.getStatus().get().equals(status)).collect(Collectors.toList());
	    	}
	    	model.addAttribute("startdate",start_date);
	    	model.addAttribute("enddate",end_date);
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
	    	Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
	    	
	    	
	    	LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
			model.addAttribute("updateleave", lhd); 
			return "edit_leave";
		}
	    
	    @RequestMapping(path = "leave/leave_detail/{leave_history_id}", method = RequestMethod.GET)
	   	public String leaveDetail(Model model,@PathVariable(value = "leave_history_id") String leave_history_id) {
	    	Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
	    	
	    	LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
	   		model.addAttribute("leave_detail", lhd); 
	   		return "leave_detail";
	   	}
	    
		@RequestMapping(path = "leave/update_leave", method=RequestMethod.GET)
		public String saveleavestatus(@RequestParam("action")String action,@RequestParam("leaveHistoryId")String leaveHistoryId,@RequestParam("reasons") String reasons ) {
	    	Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Manager"))
				return "redirect:/logout";
			
			
			LeaveHistoryDetails ldh = leaveHistoryDetailsRepo.findById(Integer.valueOf(leaveHistoryId)).orElse(null);
			Employee emp = ldh.getEmployee();
			String leave_type =ldh.getLeaveType().getType();
			
			System.out.println("*******************");
	    	System.out.println("Size "+emp.getEmail());

			if(action.equals("2"))
			{
				ldh.setStatus(Status.REJECTED);
				ldh.setRejectionReason(reasons);
				sendMail(emp.getEmail(),Status.REJECTED.get(),intro+reasons);
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
				 sendMail(emp.getEmail(),Status.APPROVED.get(),"Ok.I accept.");

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
	         msg.setTo("butterflygirl199@gmail.com");
	         msg.setFrom("hninnwe.coder@gmail.com");

	         msg.setSubject("LEAVE "+status);
	         msg.setText(reasons);

	         javaMailSender.send(msg);
		}
		
	}

