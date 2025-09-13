package com.example.simpledriveproject.modules.drive.service.Iservice;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;

public interface DriveService {
    FileResponse uploadFile(UploadRequest request);
    FileResponse getFile(String id);

}

