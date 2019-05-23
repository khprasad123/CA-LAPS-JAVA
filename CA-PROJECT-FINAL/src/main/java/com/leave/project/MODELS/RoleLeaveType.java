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
public class RoleLeaveType {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int roleLeaveId;
	
	@ManyToOne//(cascade=CascadeType.ALL)
	@JoinColumn(name="RoleId")
	private Role role;
	
	@NotNull
	@ManyToOne//(cascade=CascadeType.ALL)
	@JoinColumn(name="Leave_Type_Id")
	private LeaveType leaveType;
	@NotNull
	private int noOfDays;
	
	
	
	///getters and setters
	
	
	
	public RoleLeaveType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoleLeaveType(Role role, @NotNull LeaveType leaveType, @NotNull int noOfDays) {
		super();
		this.role = role;
		this.leaveType = leaveType;
		this.noOfDays = noOfDays;
	}
	public int getRoleLeaveId() {
		return roleLeaveId;
	}
	public void setRoleLeaveId(int roleLeaveId) {
		this.roleLeaveId = roleLeaveId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	@Override
	public String toString() {
		return "RoleLeaveType [roleLeaveId=" + roleLeaveId + ", role=" + role + ", leaveType=" + leaveType
				+ ", noOfDays=" + noOfDays + "]";
	} 
	
	
	
}
