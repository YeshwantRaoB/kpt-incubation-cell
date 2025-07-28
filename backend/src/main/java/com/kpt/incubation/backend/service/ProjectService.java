package com.kpt.incubation.backend.service;

import com.kpt.incubation.backend.entity.Project;
import com.kpt.incubation.backend.entity.Startup;
import com.kpt.incubation.backend.exception.ResourceAlreadyExistsException;
import com.kpt.incubation.backend.exception.ResourceNotFoundException;
import com.kpt.incubation.backend.repository.ProjectRepository;
import com.kpt.incubation.backend.repository.StartupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final StartupRepository startupRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, StartupRepository startupRepository) {
        this.projectRepository = projectRepository;
        this.startupRepository = startupRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getFeaturedProjects() {
        return projectRepository.findByIsFeaturedTrue();
    }

    public List<Project> getProjectsByStartup(Long startupId) {
        return projectRepository.findByStartupId(startupId);
    }

    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public Project createProject(Project project, Long startupId) {
        // Check if project with the same name already exists
        if (projectRepository.existsByName(project.getName())) {
            throw new ResourceAlreadyExistsException("Project with name '" + project.getName() + "' already exists");
        }

        // Set the startup for the project
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new ResourceNotFoundException("Startup not found with id: " + startupId));
        
        project.setStartup(startup);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = getProjectById(id);
        
        // Check if the new name is already in use by another project
        if (!project.getName().equals(projectDetails.getName()) && 
            projectRepository.existsByName(projectDetails.getName())) {
            throw new ResourceAlreadyExistsException("Project with name '" + projectDetails.getName() + "' already exists");
        }
        
        // Update all fields
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setCoverImageUrl(projectDetails.getCoverImageUrl());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setStatus(projectDetails.getStatus());
        project.setProjectUrl(projectDetails.getProjectUrl());
        project.setGithubUrl(projectDetails.getGithubUrl());
        project.setTechnologies(projectDetails.getTechnologies());
        project.setFeatured(projectDetails.isFeatured());
        
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }
    
    public void toggleFeaturedStatus(Long id) {
        Project project = getProjectById(id);
        project.setFeatured(!project.isFeatured());
        projectRepository.save(project);
    }
}
