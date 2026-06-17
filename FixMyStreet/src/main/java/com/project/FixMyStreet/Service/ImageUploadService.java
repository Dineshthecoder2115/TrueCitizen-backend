package com.project.FixMyStreet.Service;


import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public String uploadImage(
            MultipartFile file) {

        try {
            System.out.println(
                    "File Name = " + file.getOriginalFilename()
            );

            System.out.println(
                    "File Size = " +
                            (file.getSize() / 1024.0 / 1024.0) +
                            " MB"
            );

            long start = System.currentTimeMillis();


            Map uploadResult =
                    cloudinary.uploader()
                            .upload(
                                    file.getBytes(),
                                    Map.of()
                            );
            System.out.println(
                    "Cloudinary Upload Time = " +
                            (System.currentTimeMillis() - start) +
                            " ms"
            );

            return uploadResult
                    .get("secure_url")
                    .toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to upload image",
                    e
            );
        }
    }
}
