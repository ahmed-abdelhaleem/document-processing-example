package com.example.spring.entities.document;

import com.example.spring.entities.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documents_id_sequence")
    @SequenceGenerator(name = "documents_id_sequence", sequenceName = "documents_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "sid", unique = true, updatable = false, nullable = false)
    private String sid;

    @Column(name = "type", updatable = false, nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user;
}
