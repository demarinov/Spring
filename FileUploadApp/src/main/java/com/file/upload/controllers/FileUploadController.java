package com.file.upload.controllers;

import com.file.upload.storage.FileStorageCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileUploadController {

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @CrossOrigin
    public @ResponseBody String uploadFile(@RequestParam("email") String email,
                                           @RequestPart("file") List<MultipartFile> files) throws IOException {

        int uploadedFiles = 0;

        if (files != null) {

            for (MultipartFile file : files) {

                System.out.println("Content type: "+file.getContentType());
                System.out.println("Filename: "+file.getOriginalFilename());
                System.out.println("Email: "+email);
                System.out.println("Size: "+ file.getSize());

                FileStorageCache.cache.put(file.getOriginalFilename(), file);
                uploadedFiles++;
            }
        }

        String message = String.format("%s has uploaded %d ", email, uploadedFiles);
        return uploadedFiles == 1 ? message + "file" : message + "files";
    }
}