package com.example.simpledriveproject.modules.utils.audit.repo;

import com.example.simpledriveproject.modules.utils.audit.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepo extends JpaRepository<FileMetadata, String> {
}
