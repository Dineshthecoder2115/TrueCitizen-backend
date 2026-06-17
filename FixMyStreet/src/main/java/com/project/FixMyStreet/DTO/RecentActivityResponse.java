package com.project.FixMyStreet.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentActivityResponse {

    private String title;
    private String status;
    private LocalDateTime updatedAt;

    public RecentActivityResponse(
            String title,
            String status,
            LocalDateTime updatedAt) {

        this.title = title;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // getters setters
}