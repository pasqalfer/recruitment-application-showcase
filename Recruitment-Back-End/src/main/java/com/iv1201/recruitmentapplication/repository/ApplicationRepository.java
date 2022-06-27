package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT app FROM Application app JOIN ApplicationCompetence competences on app.id = competences.application.id WHERE competences.competence.competence = :competence")
    Page<Application> findAllByCompetence(String competence, Pageable pageable);

    @Query("SELECT app FROM Application app JOIN ApplicationCompetence competences on app.id = competences.application.id WHERE competences.competence.competence in :competence")
    Page<Application> findAllByCompetence(List<String> competence, Pageable pageable);

    @Query("SELECT app FROM Application app")
    Page<Application> findAll(Pageable pageable);

    @Query("SELECT app FROM Application app JOIN Availability availability on app.id = availability.application.id WHERE NOT (availability.dateFrom > :dateTo OR availability.dateTo <:dateFrom)")
    Page<Application> findAllAvailableBetween(Date dateFrom, Date dateTo, Pageable pageable);

    @Modifying
    @Query("update Application a set a = :application where a.id = :#{#application.id} and a.version = :#{#application.version}")
    void update(Application application);
}
