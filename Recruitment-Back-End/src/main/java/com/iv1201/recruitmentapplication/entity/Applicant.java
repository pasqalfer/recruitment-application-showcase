package com.iv1201.recruitmentapplication.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true, nullable = false)
    private String pnr;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;

    @OneToOne
    private Application application;

}
