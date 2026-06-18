package com.satecho.agrosafe.platform.apiservice.iam.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SeedRolesCommand;

/**
 * Role command service
 * <p>
 *     This interface represents the service to handle role commands.
 * </p>
 */
public interface RoleCommandService {
    /**
     * Handle seed roles command
     * @param command the {@link SeedRolesCommand} command
     */
    void handle(SeedRolesCommand command);
}