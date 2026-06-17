package com.project.FixMyStreet.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecentComplaintResponse {

    private String id;
    private String title;
    private String status;
    private LocalDateTime createdAt;
}