package com.iv1201.recruitmentapplication.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String competence;

    @Override
    public boolean equals(Object o) {
        if (Objects.isNull(o))
            return false;
        else if (!(o instanceof Competence))
            return false;
        else {
            if (o instanceof Competence) {
                Competence c = (Competence) o;
                return !this.competence.equals(c.getCompetence());
            }
            return false;
        }
    }
}
