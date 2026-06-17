package com.project.FixMyStreet.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfficerStatsResponse {

    private long assigned;
    private long inProgress;
    private long resolved;
    private long rejected;

    // getters setters
}
