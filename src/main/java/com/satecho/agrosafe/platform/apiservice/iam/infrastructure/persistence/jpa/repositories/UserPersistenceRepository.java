package com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for IAM user persistence entities.
 */
@Repository
public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Long> {

    /**
     * This method is responsible for finding the user by username.
     * @param email The email.
     * @return The user object.
     */
    Optional<UserPersistenceEntity> findByEmail(String email);

    /**
     * This method is responsible for checking if the user exists by username.
     * @param email The username.
     * @return True if the user exists, false otherwise.
     */
    boolean existsByEmail(String email);

    Optional<UserPersistenceEntity> findByVerificationToken(String verificationToken);

    Optional<UserPersistenceEntity> findByResetToken(String resetToken);

    boolean existsByRegistrationNumber(String registrationNumber);
}