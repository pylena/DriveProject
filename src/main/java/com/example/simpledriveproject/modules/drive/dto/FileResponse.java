package com.example.simpledriveproject.modules.drive.dto;
import lombok.Data;
import java.time.Instant;

@Data
public class FileResponse {
    private String id; private String data;
    private Long fileSize;
    private Instant created_at;
}
