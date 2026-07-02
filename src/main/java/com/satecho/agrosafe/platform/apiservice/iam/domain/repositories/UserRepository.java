package com.satecho.agrosafe.platform.apiservice.iam.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

/**
 * IAM user repository port.
 */
public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByResetToken(String resetToken);

    boolean existsByRegistrationNumber(String registrationNumber);
}