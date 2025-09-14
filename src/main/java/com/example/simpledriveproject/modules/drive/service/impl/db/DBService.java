package com.example.simpledriveproject.modules.drive.service.impl.db;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.drive.model.FileUpload;
import com.example.simpledriveproject.modules.drive.repo.FileRepo;
import com.example.simpledriveproject.modules.drive.service.Iservice.DriveService;
import com.example.simpledriveproject.modules.utils.audit.model.FileMetadata;
import com.example.simpledriveproject.modules.utils.audit.repo.FileMetadataRepo;
import com.example.simpledriveproject.modules.utils.audit.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "DB")
@RequiredArgsConstructor

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
        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(file.getId());
        fileResponse.setFileSize(size);
        fileResponse.setCreated_at(Instant.now());
        metadataService.setTracking(fileResponse);

         return fileResponse;

        }


    @Override
    public FileResponse getFile(String id) {
        try{
            FileMetadata blob =   metadataService.findById(id);
            FileUpload file = fileRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("File content not found"));

            byte[] data = file.getContent();
            FileResponse response = metadataService.
                    getResponse(id,data,  blob.getSize(), blob.getCreated_at());
            return response;

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }


