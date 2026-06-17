package com.project.FixMyStreet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {

    private long totalComplaints;

    private long pendingComplaints;

    private long assignedComplaints;

    private long inProgressComplaints;

    private long resolvedComplaints;

    private long rejectedComplaints;

    private long totalOfficers;

    private long activeOfficers;

    private long suspendedOfficers;

    private long pendingOfficers;

    private long rejectedOfficers;
}
