package com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * JPA persistence entity for IAM users.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "verified", nullable = false, columnDefinition = "boolean default true")
    private Boolean verified = true;

    @Column(name = "verification_token", length = 64)
    private String verificationToken;

    @Column(name = "reset_token", length = 64)
    private String resetToken;

    @Column(name = "reset_token_expires_at")
    private java.time.Instant resetTokenExpiresAt;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "specialty", length = 100)
    private String specialty;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "blocked", nullable = false, columnDefinition = "boolean default false")
    private Boolean blocked = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolePersistenceEntity> roles = new HashSet<>();
}