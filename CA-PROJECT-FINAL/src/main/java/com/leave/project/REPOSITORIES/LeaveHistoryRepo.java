package com.leave.project.REPOSITORIES;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leave.project.MODELS.LeaveHistoryDetails;
import com.leave.project.UTILITIES.Status;

public interface LeaveHistoryRepo extends JpaRepository<LeaveHistoryDetails, Integer>{
	List<LeaveHistoryDetails> findByStatusOrStatus(Status status,Status status1);
	List<LeaveHistoryDetails> findByStatus(Status status);
}
