package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

public record SuspendUserCommand(
        Long userId,
        String reason,
        Long suspendedBy
) {
    public SuspendUserCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }
        if (suspendedBy == null) {
            throw new IllegalArgumentException("Suspended by admin ID is required");
        }
    }
}
