package com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class User extends AuditableAbstractAggregateRoot<User> {

    @Setter private Long id;
    @Setter private String email;
    @Setter private String password;
    @Setter private String fullName;
    @Setter private Set<Role> roles;
    @Setter private Boolean verified;
    @Setter private String verificationToken;
    @Setter private Date createdAt;
    @Setter private String resetToken;
    @Setter private Instant resetTokenExpiresAt;
    @Setter private Boolean blocked;
    // Populated only for AGRONOMIST-role users (EP-001-US007).
    @Setter private String registrationNumber;
    @Setter private String specialty;
    @Setter private Integer yearsOfExperience;

    public User() {
        this.roles = new HashSet<>();
        this.verified = false;
        this.blocked = false;
    }

    public User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roles = new HashSet<>();
        this.verified = false;
        this.blocked = false;
    }

    public boolean isBlocked() {
        return Boolean.TRUE.equals(blocked);
    }

    public User(String email, String password, String fullName, List<Role> roles) {
        this(email, password, fullName);
        addRoles(roles);
    }

    /**
     * Add a role to the user
     * @param role the role to add
     * @return the user with the added role
     */
    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    /**
     * Add a list of roles to the user
     * @param roles the list of roles to add
     * @return the user with the added roles
     */
    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }

}