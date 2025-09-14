package com.example.simpledriveproject.modules.drive.repo;

import com.example.simpledriveproject.modules.drive.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepo extends JpaRepository<FileUpload, String> {
    Optional<FileUpload> findById(String id);

}