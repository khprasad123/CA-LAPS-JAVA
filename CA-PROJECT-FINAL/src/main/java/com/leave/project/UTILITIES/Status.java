package com.leave.project.UTILITIES;

public enum Status {
	APPLIED("Applied"),REJECTED("Rejected"),APPROVED("Approved"),UPDATED("Updated"),DELETED("Deleted"),CANCELLED("Cancelled");
	String status;
	Status(String status) {
		this.status = status;
	}
	String getStatus() {
		return status;
	}
}
