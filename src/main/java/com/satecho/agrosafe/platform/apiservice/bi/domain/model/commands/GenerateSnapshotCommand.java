package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;

import java.time.Instant;

public record GenerateSnapshotCommand(
        SnapshotPeriod period,
        Instant fromDate,
        Instant toDate
) {
    public GenerateSnapshotCommand {
        if (period == null) {
            throw new IllegalArgumentException("Period is required");
        }
        if (fromDate == null) {
            throw new IllegalArgumentException("From date is required");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("To date is required");
        }
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date must be before to date");
        }
    }
}
