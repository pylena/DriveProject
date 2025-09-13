package com.example.simpledriveproject.modules.drive.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class FileUpload {
    @Id
    private String id;
    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;
}
