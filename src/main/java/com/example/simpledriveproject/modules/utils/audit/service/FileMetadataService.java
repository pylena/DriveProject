package com.example.simpledriveproject.modules.utils.audit.service;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.utils.audit.model.FileMetadata;
import com.example.simpledriveproject.modules.utils.audit.repo.FileMetadataRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final FileMetadataRepo metadataRepo;

    @Transactional
    public void setTracking(FileResponse savedFile ) {
        FileMetadata metadata = new FileMetadata();
        metadata.setId(savedFile.getId());
        metadata.setSize(savedFile.getFileSize());
        metadata.setCreated_at(Instant.now());
        metadataRepo.save(metadata);

    }

    public void validId(String id){
        if(metadataRepo.existsById(id)) {
            throw new IllegalArgumentException("File with id already exists." + id );
        }
    }

    public FileMetadata findById(String id){
        return metadataRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("file not found " + id));
    }


    public FileResponse getResponse(String id, byte[] content, long size, Instant created_at) {

        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(id);
        fileResponse.setFileSize(size);
        fileResponse.setCreated_at(created_at);
        fileResponse.setData(Base64.getEncoder().encodeToString(content));
        return fileResponse;

    }

    }



