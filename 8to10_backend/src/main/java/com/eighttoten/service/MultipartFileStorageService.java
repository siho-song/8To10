package com.eighttoten.service;

import static com.eighttoten.exception.ExceptionCode.REQUIRE_FILE_NAME;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.eighttoten.exception.BadRequestException;

@Service
public class MultipartFileStorageService {

    public String saveFile(MultipartFile multipartFile, String directoryPath) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        if (originalFilename == null) {
            throw new BadRequestException(REQUIRE_FILE_NAME);
        }

        String filePath = directoryPath + generateRandomFileName(originalFilename);

        File file = new File(filePath);

        if (!file.exists()){
            createDirectoryIfNotExists(directoryPath);
            multipartFile.transferTo(file);
        }

        return filePath;
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        directory.mkdirs();
    }

    private String generateRandomFileName(String originalFileName) {
        int randomInt = ThreadLocalRandom.current().nextInt(1_000_000);
        return UUID.randomUUID() + "_" + randomInt + extractFileExtension(originalFileName);
    }

    private String extractFileExtension(String fileName) {
        String lowerCase = fileName.toLowerCase();
        return lowerCase.substring(lowerCase.lastIndexOf("."));
    }
}
