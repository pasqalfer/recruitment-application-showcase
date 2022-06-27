package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence, Long> {
    Competence findByCompetence(String competence);

    boolean existsByCompetence(String competence);

    List<Competence> findAll();
}
