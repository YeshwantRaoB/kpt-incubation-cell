package com.kpt.incubation.backend.repository;

import com.kpt.incubation.backend.entity.Project;
import com.kpt.incubation.backend.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStartup(Startup startup);
    List<Project> findByStartupId(Long startupId);
    List<Project> findByIsFeaturedTrue();
    List<Project> findByStatus(String status);
    boolean existsByName(String name);
}
