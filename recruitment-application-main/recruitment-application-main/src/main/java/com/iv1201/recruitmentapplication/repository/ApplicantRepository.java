package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.Applicant;
import com.iv1201.recruitmentapplication.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Applicant getById(long id);

    Applicant findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPnr(String pnr);

    Applicant findByEmail(String email);

    Applicant findByUsernameOrEmail(String username, String password);

    Applicant findByApplication(Application application);

    @Modifying
    @Query("update Applicant a set a = :applicant where a.id = :#{#applicant.id}")
    void update(Applicant applicant);
}
