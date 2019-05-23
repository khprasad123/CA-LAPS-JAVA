package com.leave.project.REPOSITORIES;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leave.project.MODELS.LeaveHistoryDetails;

public interface LeaveHistoryRepo extends JpaRepository<LeaveHistoryDetails, Integer>{

}
