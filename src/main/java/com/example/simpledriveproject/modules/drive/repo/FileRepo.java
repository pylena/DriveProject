package com.example.simpledriveproject.modules.drive.repo;

import com.example.simpledriveproject.modules.drive.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<FileUpload, String> {
}