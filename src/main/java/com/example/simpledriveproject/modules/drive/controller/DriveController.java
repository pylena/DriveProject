package com.example.simpledriveproject.modules.drive.controller;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.drive.service.Iservice.DriveService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class DriveController {
    final DriveService driveService;


    @PostMapping("/blobs")
    public ResponseEntity<FileResponse> uploadFile(@Valid @RequestBody UploadRequest request) throws Exception {
        FileResponse response = driveService.uploadFile(request);
        URI path = URI.create("/v1/blobs/" + response.getId());
        return ResponseEntity.created(path).body(response);
    }

    @PostMapping("/blobs/{id}")
    public ResponseEntity<String> getFile(@Valid @PathVariable String id) {
        return ResponseEntity.ok("File uploaded: " +  id);

    }
}


