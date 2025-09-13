package com.example.simpledriveproject.modules.drive.dto;

import com.example.simpledriveproject.modules.utils.validation.IsBase64;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadRequest {
    @Id
    private String id ;
    @NotBlank
    @IsBase64(message = "upload data must be Base64 ")
    private String data;
}
