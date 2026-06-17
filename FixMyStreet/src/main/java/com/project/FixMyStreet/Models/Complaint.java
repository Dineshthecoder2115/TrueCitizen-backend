package com.project.FixMyStreet.Models;

import com.project.FixMyStreet.Enums.ComplaintPriority;
import com.project.FixMyStreet.Enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    private String id;

    private String title;

    private String description;

    @Indexed
    private String category;

    private String imageUrl;

    // Citizen who created the complaint
    @Indexed
    private String citizenProfileId;

    // Department responsible
    @Indexed
    private String  department;

    // Assigned officer
    @Indexed
    private String assignedOfficerProfileId;
    @Indexed
    private ComplaintStatus status;

    private ComplaintPriority priority;

    // Location details
    private String address;

    private String city;

    private String pincode;

    private Double latitude;

    private Double longitude;

    // Officer/Admin remarks
    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime assignedAt;

    private LocalDateTime resolvedAt;

    private Integer rating;

    private String feedback;

    private LocalDateTime reviewedAt;
}