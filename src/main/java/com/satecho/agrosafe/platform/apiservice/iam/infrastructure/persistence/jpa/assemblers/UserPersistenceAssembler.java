package com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Static assembler between IAM user domain and persistence representations.
 */
public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new User();
        domain.setId(entity.getId());
        domain.setEmail(entity.getEmail());
        domain.setPassword(entity.getPassword());
        domain.setFullName(entity.getFullName());
        domain.setVerified(entity.getVerified() != null ? entity.getVerified() : true);
        domain.setVerificationToken(entity.getVerificationToken());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setResetToken(entity.getResetToken());
        domain.setResetTokenExpiresAt(entity.getResetTokenExpiresAt());
        domain.setRegistrationNumber(entity.getRegistrationNumber());
        domain.setSpecialty(entity.getSpecialty());
        domain.setYearsOfExperience(entity.getYearsOfExperience());
        domain.setBlocked(entity.getBlocked() != null ? entity.getBlocked() : false);
        domain.setRoles(entity.getRoles() == null
                ? new HashSet<>()
                : entity.getRoles().stream()
                .map(RolePersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toSet()));
        return domain;
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) {
            entity.setId(user.getId());
        }
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setVerified(user.getVerified() != null ? user.getVerified() : true);
        entity.setVerificationToken(user.getVerificationToken());
        entity.setResetToken(user.getResetToken());
        entity.setResetTokenExpiresAt(user.getResetTokenExpiresAt());
        entity.setRegistrationNumber(user.getRegistrationNumber());
        entity.setSpecialty(user.getSpecialty());
        entity.setYearsOfExperience(user.getYearsOfExperience());
        entity.setBlocked(user.getBlocked() != null ? user.getBlocked() : false);
        entity.setRoles(user.getRoles() == null
                ? new HashSet<>()
                : user.getRoles().stream()
                .map(RolePersistenceAssembler::toPersistenceFromDomain)
                .collect(Collectors.toSet()));
        return entity;
    }
}