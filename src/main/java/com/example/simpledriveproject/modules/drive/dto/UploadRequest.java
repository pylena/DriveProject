package com.example.simpledriveproject.modules.drive.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class UploadRequest {
    @Id
    private String id ;
    @NotBlank
    @IsBase64(message = "upload data must be Base64 ")
    private String data;
}
