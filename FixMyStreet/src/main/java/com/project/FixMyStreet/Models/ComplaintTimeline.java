package com.project.FixMyStreet.Models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "complaint_timeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintTimeline {

    @Id
    private String id;

    private String complaintId;

    private String status;

    private String title;

    private String description;

    private LocalDateTime createdAt;
}