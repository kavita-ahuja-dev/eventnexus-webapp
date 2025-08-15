package com.cdac.eventnexus.dto;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class DashboardSummaryDto {
    private long totalUsers;
    private long totalEvents;
    private long totalExhibitors;
    private long totalCustomers;
    private long activeEvents;
    private long activeUsers;

}

