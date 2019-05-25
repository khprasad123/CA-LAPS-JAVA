package com.leave.project.MODELS;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LeaveType {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveTypeId;
	
	private String type;
	
	
	///getters and setters
	
	
	public int getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LeaveType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveType(String type) {
		super();
		this.type = type;
	}

	@Override
	public String toString() {
		return  type;
	}
	
}
