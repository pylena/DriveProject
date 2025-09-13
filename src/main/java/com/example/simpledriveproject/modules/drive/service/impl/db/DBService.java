package com.example.simpledriveproject.modules.drive.service.impl.db;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.drive.model.FileUpload;
import com.example.simpledriveproject.modules.drive.repo.FileRepo;
import com.example.simpledriveproject.modules.drive.service.Iservice.DriveService;
import com.example.simpledriveproject.modules.utils.audit.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;


@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "db")
public class DBService implements DriveService {

    private final FileRepo fileRepo;
    private final FileMetadataService metadataService;

    @Override
    @Transactional
    public FileResponse uploadFile(UploadRequest request) {
        //validate id
        metadataService.validId(request.getId());
        // decode & cal file size
        byte[] content = Base64.getDecoder().decode(request.getData());
        long size = content.length;
        FileUpload file = new FileUpload();
        file.setId(request.getId());
        file.setContent(content);
        fileRepo.save(file);
        // track
        return metadataService.getSaveResponse(request, size); }


    @Override
    public FileResponse getFile(String id) {
        return null;
    }
    }


