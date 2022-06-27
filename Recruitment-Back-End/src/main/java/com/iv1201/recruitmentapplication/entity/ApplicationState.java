package com.iv1201.recruitmentapplication.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private State state;


    public enum State {
        UNHANDLED,
        ACCEPTED,
        REJECTED
    }

}
