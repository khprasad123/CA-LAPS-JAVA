package com.leave.project.MODELS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int empId;
	@NotNull
	private String fullName;
	@NotNull
	private String email;
	@NotNull
	@Column(unique=true)
	private String userName;
	@NotNull
	private String password;
	
	@ManyToOne
	@JoinColumn(name="Role_Id")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="Reports_To")
	private Employee reportsTo;
	
	
	///getters and setters
	
	
	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Employee getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Employee reportsTo) {
		this.reportsTo = reportsTo;
	}

	public Employee() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	public Employee(String fullName, String email, String userName, String password, Role role,
			Employee reportsTo) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.reportsTo = reportsTo;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", fullName=" + fullName + ", email=" + email + ", userName=" + userName
				+ ", password=" + password + ", role=" + role.toString() + ", reportsTo=" + reportsTo.getFullName() + "]";
	}
	
	
	
}


