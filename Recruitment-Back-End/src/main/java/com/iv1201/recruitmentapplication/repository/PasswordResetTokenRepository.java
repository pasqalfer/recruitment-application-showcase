package com.iv1201.recruitmentapplication.repository;

import com.iv1201.recruitmentapplication.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This represents the repository for the password reset token.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findPasswordResetTokenByToken(String token);
}
