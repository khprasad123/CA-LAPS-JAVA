package com.leave.project.MODELS;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.leave.project.UTILITIES.Status;

@Entity
public class LeaveHistoryDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveHistoryId;
	@ManyToOne
	@JoinColumn(name="Emp_Id")
	private Employee employee;
	@ManyToOne
	@JoinColumn(name="Leave_Type_Id")
	private LeaveType leaveType;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	@NotNull
	private String applyingReason;
	private String rejectionReason;
	@NotNull
	private Status status=Status.APPLIED;
	private String workDesemination;
	
	
	///getters and setters
	
	
	public int getLeaveHistoryId() {
		return leaveHistoryId;
	}
	public void setLeaveHistoryId(int leaveHistoryId) {
		this.leaveHistoryId = leaveHistoryId;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getApplyingReason() {
		return applyingReason;
	}
	public void setApplyingReason(String applyingReason) {
		this.applyingReason = applyingReason;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getWorkDesemination() {
		return workDesemination;
	}
	public void setWorkDesemination(String workDesemination) {
		this.workDesemination = workDesemination;
	}
	public LeaveHistoryDetails(int leaveHistoryId, Employee employee, LeaveType leaveType, @NotNull Date startDate,
			@NotNull Date endDate, @NotNull String applyingReason, String rejectionReason, @NotNull Status status,
			String workDesemination) {
		super();
		this.leaveHistoryId = leaveHistoryId;
		this.employee = employee;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.applyingReason = applyingReason;
		this.rejectionReason = rejectionReason;
		this.status = status;
		this.workDesemination = workDesemination;
	}
	public LeaveHistoryDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
