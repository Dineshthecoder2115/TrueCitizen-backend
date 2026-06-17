package com.project.FixMyStreet.DTO;



import com.project.FixMyStreet.Enums.ComplaintStatus;
import lombok.Data;

@Data
public class ComplaintStatusUpdateRequest {

    private ComplaintStatus status;

    private String remarks;
}