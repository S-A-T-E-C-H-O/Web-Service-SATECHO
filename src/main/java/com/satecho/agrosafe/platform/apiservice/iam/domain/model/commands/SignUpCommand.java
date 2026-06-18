package com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;

import java.util.List;

/**
 * Sign up command
 * <p>
 *     This class represents the command to sign up a user.
 * </p>
 * @param fullName the full name of the user
 * @param email the email of the user
 * @param password the password of the user
 * @param roles the roles of the user
 *
 * @see com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User
 */
public record SignUpCommand(String fullName, String email, String password, List<Role> roles) {
}