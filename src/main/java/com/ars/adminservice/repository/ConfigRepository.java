package com.ars.adminservice.repository;

import com.ars.adminservice.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Optional<Config> findByCode(String code);
    Optional<Config> findByCodeAndShopId(String code, Integer shopId);
}
