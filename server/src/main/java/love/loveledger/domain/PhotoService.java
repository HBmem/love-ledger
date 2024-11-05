package love.loveledger.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PhotoService {
    @Value("${upload.path}")
    private String uploadPath;

    public String savePhoto(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }
}
