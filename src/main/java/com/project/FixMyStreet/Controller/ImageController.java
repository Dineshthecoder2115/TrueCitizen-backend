package com.project.FixMyStreet.Controller;


import com.project.FixMyStreet.Service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public Map<String,String> uploadImage(
            @RequestParam("file")
            MultipartFile file) {

        String imageUrl =
                imageUploadService
                        .uploadImage(file);

        return Map.of(
                "imageUrl",
                imageUrl
        );
    }
}
