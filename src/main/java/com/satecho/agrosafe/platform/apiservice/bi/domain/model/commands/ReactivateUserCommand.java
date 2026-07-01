package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

public record ReactivateUserCommand(
        Long userId,
        Long reactivatedBy
) {
    public ReactivateUserCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (reactivatedBy == null) {
            throw new IllegalArgumentException("Reactivated by admin ID is required");
        }
    }
}
