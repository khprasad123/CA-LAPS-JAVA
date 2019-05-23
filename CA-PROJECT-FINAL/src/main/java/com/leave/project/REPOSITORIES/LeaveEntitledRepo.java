package com.leave.project.REPOSITORIES;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.LeaveEntitled;

@Repository
public interface LeaveEntitledRepo extends JpaRepository<LeaveEntitled, Integer>{

}
