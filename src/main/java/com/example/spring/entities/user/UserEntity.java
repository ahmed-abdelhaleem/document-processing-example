package com.example.spring.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_sequence")
    @SequenceGenerator(name = "users_id_sequence", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "sid", unique = true, updatable = false, nullable = false)
    private String sid;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
