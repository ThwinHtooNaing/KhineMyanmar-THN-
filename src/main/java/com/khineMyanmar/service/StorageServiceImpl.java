package com.khineMyanmar.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private final String UPLOAD_DIR = "src/main/resources/static/img/profiles/users";

    @Override
    public String saveProfilePicture(MultipartFile file, String firstName, String lastName, long userId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or null");
        }

        try {
           
            String sanitizedName = (firstName + "_" + lastName + "_" + userId).toLowerCase().replaceAll("\\s+", "");

            Path userDir = Paths.get(UPLOAD_DIR, sanitizedName);
            Files.createDirectories(userDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = userDir.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return "/img/profiles/users/" + sanitizedName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving profile picture", e);
        }
    }
}
