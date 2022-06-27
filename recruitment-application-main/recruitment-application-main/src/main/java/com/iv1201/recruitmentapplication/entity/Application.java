package com.iv1201.recruitmentapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application extends TimestampedTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationState applicationState;

    @OneToMany
    private List<ApplicationCompetence> competences;

    @OneToMany
    private List<Availability> availability;


}
