package com.leave.project.REPOSITORIES;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.PublicHollyday;
@Repository
public interface PublicHollydayRepo extends JpaRepository<PublicHollyday, Integer>{

}
