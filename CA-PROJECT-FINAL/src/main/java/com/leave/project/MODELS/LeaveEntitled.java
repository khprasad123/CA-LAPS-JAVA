package com.leave.project.MODELS;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class LeaveEntitled {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveEntitledId;
	@NotNull
	@ManyToOne
	@JoinColumn(name="Employee_Id")
	private Employee employee;
	@NotNull
	@ManyToOne//(cascade=CascadeType.ALL)
	@JoinColumn(name="Leave_Type_Id")
	private LeaveType leaveType;
	@NotNull
	private int remainingLeave;
	
	
	///getters and setters
	
	
	public int getLeaveEntitledId() {
		return leaveEntitledId;
	}
	public void setLeaveEntitledId(int leaveEntitledId) {
		this.leaveEntitledId = leaveEntitledId;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public int getRemainingLeave() {
		return remainingLeave;
	}
	public void setRemainingLeave(int remainingLeave) {
		this.remainingLeave = remainingLeave;
	}
	public LeaveEntitled() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeaveEntitled(int leaveEntitledId, @NotNull Employee employee, @NotNull LeaveType leaveType,
			@NotNull int remainingLeave) {
		super();
		this.leaveEntitledId = leaveEntitledId;
		this.employee = employee;
		this.leaveType = leaveType;
		this.remainingLeave = remainingLeave;
	}
	
}
