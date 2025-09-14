package com.example.simpledriveproject.modules.drive.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "filesUpload")
public class FileUpload {
    @Id
    private String id;
    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;
}
