package com.example.nosey.services;

import com.example.nosey.exception.MediaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MediaService {

    private static final String UPLOAD_DIR = "uploads";

    public String uploadMedia(MultipartFile media) throws IOException {
        if (media == null || media.isEmpty()) {
            return null;
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String mediaPath = UPLOAD_DIR + "/" + media.getOriginalFilename();
            Files.copy(media.getInputStream(), Paths.get(mediaPath), StandardCopyOption.REPLACE_EXISTING);
            return mediaPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save media file", e);
        }
    }
}