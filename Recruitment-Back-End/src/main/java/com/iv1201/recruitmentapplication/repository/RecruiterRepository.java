package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Recruiter findByUsername(String username);
}
