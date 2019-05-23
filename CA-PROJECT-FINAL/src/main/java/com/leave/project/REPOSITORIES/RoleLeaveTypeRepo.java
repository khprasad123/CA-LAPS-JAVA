package com.leave.project.REPOSITORIES;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.RoleLeaveType;

@Repository
public interface RoleLeaveTypeRepo extends JpaRepository<RoleLeaveType, Integer> {

}
