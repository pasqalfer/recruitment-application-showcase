package com.iv1201.recruitmentapplication.entity;

import lombok.*;

import javax.persistence.*;

/**
 * This entity represents a token used when an applicant wants to reset their password.
 * The token is put in the database together with the corresponding applicant.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private Applicant applicant;
}
