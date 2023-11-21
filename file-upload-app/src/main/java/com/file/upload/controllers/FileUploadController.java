package com.file.upload.controllers;

import com.file.upload.storage.FileStorageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/file")
@Slf4j
public class FileUploadController {

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @CrossOrigin
    public @ResponseBody String uploadFile(@RequestParam("email") String email,
                                           @RequestPart("file") List<MultipartFile> files) throws IOException {

        int uploadedFiles = 0;

        if (files != null) {

            for (MultipartFile file : files) {

                log.debug("Content type: " + file.getContentType());
                log.debug("Filename: " + file.getOriginalFilename());
                log.debug("Email: " + email);
                log.debug("Size: " + file.getSize());

                if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                    FileStorageCache.cache.put(file.getOriginalFilename(), file);
                    uploadedFiles++;
                }
            }
        }

        String message = String.format("%s has uploaded %d ", email, uploadedFiles);
        String control = "<button type=\"button\" onclick=\"history.back();\">Back</button>";
        return uploadedFiles == 1 ? message + "file "+control : message + "files "+control;
    }
}