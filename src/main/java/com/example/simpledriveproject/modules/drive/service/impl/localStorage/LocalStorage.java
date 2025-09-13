package com.example.simpledriveproject.modules.drive.service.impl.localStorage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service

public class LocalStorage {

    private final LocalStorageProperties properties;
    private final Path rootPath;

    public LocalStorage(LocalStorageProperties properties) {
        this.properties = properties;
        this.rootPath = Paths.get(properties.getDirPath());
    }

    public void saveFile( String id , byte[] content ) {
        try {
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            Path filePath = rootPath.resolve(id);
            Files.write(filePath, content);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to upload file: " + id, e);
        }

    }

    public Resource retrieveFile(String path) throws IOException {
        Path filePath = rootPath.resolve(path).normalize().toAbsolutePath();
        Path normalizedRoot = rootPath.normalize().toAbsolutePath();

        if(!filePath.startsWith(normalizedRoot)) {
            throw new SecurityException("Access denied");
        }
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found");
        }

        return new UrlResource(filePath.toUri());

    }

}