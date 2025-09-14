package com.example.simpledriveproject.modules.drive.service.impl.localStorage;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.drive.service.Iservice.DriveService;
import com.example.simpledriveproject.modules.utils.audit.model.FileMetadata;
import com.example.simpledriveproject.modules.utils.audit.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalStorageService implements DriveService {
    private final FileMetadataService metadataService;
    private final LocalStorage storage;

    @Override
    public FileResponse uploadFile(UploadRequest request) {
        //validate id
        metadataService.validId(request.getId());

        // decode & cal file size
        try {
            byte[] content = Base64.getDecoder().decode(request.getData());
            long size = content.length;
            storage.saveFile(request.getId(), content);
            //track
            FileResponse fileResponse = new FileResponse();
            fileResponse.setId(request.getId());
            fileResponse.setFileSize(size);
            fileResponse.setCreated_at(Instant.now());
            metadataService.setTracking(fileResponse);
            return fileResponse;

        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file"); }


    }

    @Override
    public FileResponse getFile(String id) {
        try{
            FileMetadata blob =   metadataService.findById(id);
            byte[] data = storage.retrieveFile(id);
            FileResponse response = metadataService.
                    getResponse(id,data,  blob.getSize(), blob.getCreated_at());
            return response;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
