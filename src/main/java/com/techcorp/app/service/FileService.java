package com.techcorp.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Path targetLocation = Paths.get(uploadDir).resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }
            throw new RuntimeException("Plik nie znaleziony " + fileName);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Plik nie znaleziony " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        if (fileName == null) {
            log.error("File name is null, cannot delete file.");
            throw new IllegalArgumentException("File name cannot be null");
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: " + fileName);
            } else {
                log.warn("File not found, deletion skipped: " + fileName);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: " + fileName, e);
            throw new IOException("Failed to delete file " + fileName, e);
        }
    }

}
