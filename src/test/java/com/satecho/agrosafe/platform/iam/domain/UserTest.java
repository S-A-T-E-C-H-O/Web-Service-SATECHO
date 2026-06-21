package com.satecho.agrosafe.platform.iam.domain;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User aggregate")
class UserTest {

    @Test
    @DisplayName("no-arg constructor sets verified=false and empty roles set")
    void defaultConstructor_setsVerifiedFalseAndEmptyRoles() {
        User user = new User();
        assertThat(user.getVerified()).isFalse();
        assertThat(user.getRoles()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("three-arg constructor sets email, password, fullName and verified=false")
    void threeArgConstructor_setsBasicFieldsAndVerifiedFalse() {
        User user = new User("test@example.com", "hash", "Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("hash");
        assertThat(user.getFullName()).isEqualTo("Test User");
        assertThat(user.getVerified()).isFalse();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("four-arg constructor adds provided roles")
    void fourArgConstructor_addsProvidedRoles() {
        Role farmerRole = new Role(Roles.ROLE_FARMER);
        User user = new User("a@b.com", "hash", "Full Name", List.of(farmerRole));
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles().iterator().next().getName()).isEqualTo(Roles.ROLE_FARMER);
    }

    @Test
    @DisplayName("four-arg constructor with empty roles defaults to ROLE_FARMER via validateRoleSet")
    void fourArgConstructor_emptyRoles_defaultsToFarmer() {
        User user = new User("a@b.com", "hash", "Name", List.of());
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles().iterator().next().getName()).isEqualTo(Roles.ROLE_FARMER);
    }

    @Test
    @DisplayName("addRole() adds the role and returns the user (fluent)")
    void addRole_addsRoleAndReturnsUser() {
        User user = new User();
        Role role = new Role(Roles.ROLE_AGRONOMIST);
        User returned = user.addRole(role);
        assertThat(returned).isSameAs(user);
        assertThat(user.getRoles()).containsExactly(role);
    }

    @Test
    @DisplayName("addRole() chaining adds multiple roles")
    void addRole_chaining_addsMultipleRoles() {
        User user = new User();
        Role farmer = new Role(Roles.ROLE_FARMER);
        Role admin = new Role(Roles.ROLE_ADMIN);
        user.addRole(farmer).addRole(admin);
        assertThat(user.getRoles()).containsExactlyInAnyOrder(farmer, admin);
    }

    @Test
    @DisplayName("addRoles() adds all roles in the list")
    void addRoles_addsAllProvided() {
        User user = new User();
        Role farmer = new Role(Roles.ROLE_FARMER);
        Role agronomist = new Role(Roles.ROLE_AGRONOMIST);
        user.addRoles(List.of(farmer, agronomist));
        assertThat(user.getRoles()).containsExactlyInAnyOrder(farmer, agronomist);
    }

    @Test
    @DisplayName("addRoles() with null list defaults to ROLE_FARMER via validateRoleSet")
    void addRoles_nullList_defaultsToFarmer() {
        User user = new User();
        user.addRoles(null);
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles().iterator().next().getName()).isEqualTo(Roles.ROLE_FARMER);
    }

    @Test
    @DisplayName("setters update fields correctly")
    void setters_updateFields() {
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("newHash");
        user.setFullName("New Name");
        user.setVerified(true);
        user.setVerificationToken("abc123");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("newHash");
        assertThat(user.getFullName()).isEqualTo("New Name");
        assertThat(user.getVerified()).isTrue();
        assertThat(user.getVerificationToken()).isEqualTo("abc123");
    }
}
