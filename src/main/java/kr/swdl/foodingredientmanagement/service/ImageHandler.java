package kr.swdl.foodingredientmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Component
public class ImageHandler {

    private Path saveDirectory;

    public ImageHandler(@Value("${file.upload-dir}") String saveDirectory) {
        this.saveDirectory = Paths.get(saveDirectory)
                .toAbsolutePath()
                .normalize()
                .resolve("disposalApp");
        try {
            Files.createDirectories(this.saveDirectory);
        } catch (FileAlreadyExistsException e) {
            throw new IllegalStateException(
                    "Failed to create image storage directory: A file with the same name already exists.", e);
        } catch (UnsupportedOperationException | IOException e) {
            throw new IllegalStateException(
                    "Failed to create image storage directory: Please check the server path, permissions, and file system status.", e);
        }
    }

    public String saveImage(MultipartFile file) {
        if (null == file || file.isEmpty()) {
            throw new IllegalStateException("Invalid file.");
        }
        String urn = makeUniqueName(file.getContentType());
        Path filePath = saveDirectory.resolve(urn);
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, filePath);
            return urn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImage(String urn) {
        if (null == urn || urn.isEmpty()) {
            throw new IllegalStateException("Invalid urn.");
        }
        Path filePath = saveDirectory.resolve(urn).normalize();
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeUniqueName(String contentType) {
        String type = contentType.split("/")[1];
        if (!type.equals("jpg") && !(!type.equals("png"))) {
            throw new IllegalArgumentException("This format is not supported. Please check if the media type is JPG or PNG.");
        }
        return UUID.randomUUID() + "." + type;
    }

    public String getImage(String urn) {
        if (null == urn || urn.isEmpty()) {
            throw new IllegalStateException("Invalid urn.");
        }
        Path filePath = saveDirectory.resolve(urn).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                byte[] imageBytes = resource.getContentAsByteArray();
                String contentType = Files.probeContentType(filePath);
                String base64 = Base64.getEncoder()
                        .encodeToString(imageBytes);
                return "data:" + contentType + ";base64," + base64;
            } else {
                throw new RuntimeException(
                        "The image cannot be found or cannot be read." + urn
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
