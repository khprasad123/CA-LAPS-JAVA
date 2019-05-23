package com.leave.project.UTILITIES;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.LeaveType;
import com.leave.project.MODELS.Role;
import com.leave.project.MODELS.RoleLeaveType;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.LeaveEntitledRepo;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
import com.leave.project.REPOSITORIES.RoleLeaveTypeRepo;
import com.leave.project.REPOSITORIES.RoleRepo;

@Component
public class DataLoader implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());
	private RoleRepo roleRepo;

	@Autowired
	public void setRoleRepo(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	private EmployeeRepo empRepo;

	@Autowired
	public void setRoleRepo(EmployeeRepo roleRepo) {
		this.empRepo = roleRepo;
	}
	private LeaveTypeRepo leaveTrepo;
	@Autowired
	public void setLeaveTrepo(LeaveTypeRepo leaveTrepo) {
		this.leaveTrepo = leaveTrepo;
	}
	
	private RoleLeaveTypeRepo roleLeave;
	@Autowired
	public void setRoleLeave(RoleLeaveTypeRepo roleLeave) {
		this.roleLeave = roleLeave;
	}
	private LeaveEntitledRepo leaveEntitiledRepo;
	@Autowired
	public void setLeaveEntitiledRepo(LeaveEntitledRepo leaveEntitiledRepo) {
		this.leaveEntitiledRepo = leaveEntitiledRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("ApplicationStartupRunner run method Started !!");
		
		  Role r1=new Role("Manager"); Role r2=new Role("Admin"); Role r3=new
		  Role("Staff"); 
		  Employee E1=new Employee("ADMIN","kannan@email.com","admin","admin",r2, null);
		  E1.setReportsTo(E1);
		  Employee E2=new Employee("Hari ","hari@email.com","khprasad143","123",r1, E1);
		  
		  
		  try{ roleRepo.save(r1); roleRepo.save(r2); roleRepo.save(r3);
		  }catch(Exception e) { System.out.println("Role Already there Bro"); }
		  
		  try { empRepo.save(E1); empRepo.save(E2); }catch(Exception e) {
		  System.out.println("Manager insertion Failed bro"); }
		
		  
		  LeaveType L1=new LeaveType("Medical");
		  LeaveType L2=new LeaveType("Anual");
		  LeaveType L3=new LeaveType("Compensation");
		  leaveTrepo.save(L1);leaveTrepo.save(L2);leaveTrepo.save(L3);
		  
		  RoleLeaveType R1=new RoleLeaveType(r1,L1, 30);
		  RoleLeaveType R2=new RoleLeaveType(r1,L2, 60);
		  RoleLeaveType R3=new RoleLeaveType(r1,L3, 100);
		  
		/*
		 * RoleLeaveType R4=new RoleLeaveType(r2,L1, 30); RoleLeaveType R5=new
		 * RoleLeaveType(r2,L2, 60); RoleLeaveType R6=new RoleLeaveType(r2,L3, 90);
		 */
		 
		  RoleLeaveType R8=new RoleLeaveType(r3,L1, 30); 
		  RoleLeaveType R9=new RoleLeaveType(r3,L2, 60); 
		  RoleLeaveType R10=new RoleLeaveType(r3,L3, 80);
		  
		  roleLeave.save(R1);roleLeave.save(R2);roleLeave.save(R3);roleLeave.save(R8);roleLeave.save(R9);roleLeave.save(R10);
		  
		  
	  
	}
}
