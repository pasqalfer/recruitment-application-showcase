package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.ApplicationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationStateRepository extends JpaRepository<ApplicationState, Long> {
    ApplicationState findByState(ApplicationState.State state);
}
