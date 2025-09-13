package com.example.simpledriveproject.modules.drive.service.impl.localStorage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.local-storage")
@Component
public class LocalStorageProperties {

    private String dirPath = "/home/server_storage";

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }


}