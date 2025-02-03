package com.portal.security.repository;

import com.portal.security.entity.OneTimeTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends JpaRepository<OneTimeTokenEntity, Long> {

    Optional<OneTimeTokenEntity> findByTokenValueAndUsedFalse(String tokenValue);
}
