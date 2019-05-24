package com.leave.project.UTILITIES;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.Role;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.LeaveTypeRepo;
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

	@Override
	public void run(String... args) throws Exception {
		logger.info("ApplicationStartupRunner run method Started !!");
		
		  Role r1=new Role("Manager"); Role r2=new Role("Admin"); Role r3=new
		  Role("Staff"); 
		  Employee E1=new Employee("ADMIN","kannan@email.com","admin","admin",r2, null,30,10,10);
		  E1.setReportsTo(E1); 
		  
		  Employee E2=new Employee("Hari ","hari@email.com","khprasad143","123",r1, E1,30,10,10);
		  
		  
		  try{ roleRepo.save(r1); roleRepo.save(r2); roleRepo.save(r3);
		  }catch(Exception e) { System.out.println("Role Already there Bro"); }
		 
		  try { empRepo.save(E1); empRepo.save(E2); }catch(Exception e) {
		  System.out.println("Manager insertion Failed bro"); }
		 
	}
}
