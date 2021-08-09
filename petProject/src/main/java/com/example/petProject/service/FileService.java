package com.example.petProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public static String saveFile(MultipartFile file, final String DIR_PATH) {

        File directory = Paths.get(DIR_PATH).toFile();

        if (!directory.exists()) {
            directory.mkdir();
        }

        String randomName = UUID.randomUUID().toString();
        String result = randomName + "." + file.getOriginalFilename();
        File techReqs = Paths.get(DIR_PATH + "/" + result).toFile();

        try {
            file.transferTo(techReqs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean deleteFile(String name, String DIR_PATH) {

        File direc = Paths.get(DIR_PATH + "/" + name).toFile();
        return direc.delete();
    }

}
