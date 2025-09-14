package com.example.simpledriveproject.modules.drive.service.impl.S3;

import com.example.simpledriveproject.modules.drive.dto.FileResponse;
import com.example.simpledriveproject.modules.drive.dto.UploadRequest;
import com.example.simpledriveproject.modules.drive.service.Iservice.DriveService;
import com.example.simpledriveproject.modules.utils.audit.model.FileMetadata;
import com.example.simpledriveproject.modules.utils.audit.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "S3")
@RequiredArgsConstructor

public class S3Service implements DriveService {
    private final S3Config s3;
    private final FileMetadataService metadataService;


    @Override
    public FileResponse uploadFile(UploadRequest request)  {
        try {
            byte[] content = Base64.getDecoder().decode(request.getData());
            long size = content.length;
            s3.upload(request.getId(), content);
            //track
            FileResponse fileResponse = new FileResponse();
            fileResponse.setId(request.getId());
            fileResponse.setFileSize(size);
            fileResponse.setCreated_at(Instant.now());
            fileResponse.setData(request.getData());
            metadataService.setTracking(fileResponse);
            return fileResponse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileResponse getFile(String id) {
       try {
           FileMetadata blob =   metadataService.findById(id);
           byte[] data = s3.getFile(id);
           FileResponse response = metadataService.
                  getResponse(id,data,  blob.getSize(), blob.getCreated_at());
             return response;
       }
       catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
