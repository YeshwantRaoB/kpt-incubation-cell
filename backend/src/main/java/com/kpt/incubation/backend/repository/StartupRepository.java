package com.kpt.incubation.backend.repository;

import com.kpt.incubation.backend.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByIsActiveTrueOrderByName();
    List<Startup> findByIsFeaturedTrueAndIsActiveTrue();
    List<Startup> findByIndustryAndIsActiveTrue(String industry);
    boolean existsByName(String name);
}
