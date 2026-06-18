package com.satecho.agrosafe.platform.apiservice.iam.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.RoleCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SeedRolesCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Implementation of {@link RoleCommandService}.
 * Handles role seeding commands.
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {

    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Seeds the roles into the database if they don't already exist.
     * @param command the seed roles command
     */
    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }
}