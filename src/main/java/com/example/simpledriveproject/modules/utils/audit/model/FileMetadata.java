package com.example.simpledriveproject.modules.utils.audit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class FileMetadata {
    @Id
    private String id;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant created_at;
    private long size;



}
