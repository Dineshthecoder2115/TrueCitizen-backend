package com.project.FixMyStreet.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplaintStatsResponse {

    private long total;
    private long pending;
    private long inProgress;
    private long resolved;
}