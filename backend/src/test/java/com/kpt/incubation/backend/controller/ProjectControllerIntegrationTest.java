package com.kpt.incubation.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.entity.Project;
import com.kpt.incubation.backend.entity.Startup;
import com.kpt.incubation.backend.repository.AdminRepository;
import com.kpt.incubation.backend.repository.ProjectRepository;
import com.kpt.incubation.backend.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private Long testStartupId;
    private final String TEST_EMAIL = "projecttest@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() throws Exception {
        // Clean up test data
        projectRepository.deleteAll();
        startupRepository.deleteAll();
        adminRepository.deleteAll();

        // Create a test admin
        Admin admin = new Admin();
        admin.setEmail(TEST_EMAIL);
        admin.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        admin.setName("Project Test Admin");
        admin.setActive(true);
        adminRepository.save(admin);

        // Create a test startup
        Startup startup = new Startup();
        startup.setName("Test Startup");
        startup.setIndustry("Technology");
        startup = startupRepository.save(startup);
        testStartupId = startup.getId();

        // Login to get JWT token
        AuthRequest authRequest = new AuthRequest(TEST_EMAIL, TEST_PASSWORD);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        authToken = "Bearer " + authResponse.getToken();
    }

    @Test
    public void testCreateAndGetProject() throws Exception {
        // Create a new project
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project");
        project.setStatus("In Progress");
        project.setTechnologies("Java, Spring Boot, React");
        project.setFeatured(true);

        // Test create with startup ID
        MvcResult createResult = mockMvc.perform(post("/api/projects?startupId=" + testStartupId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())))
                .andExpect(jsonPath("$.featured", is(true)))
                .andExpect(jsonPath("$.startupId", is(testStartupId.intValue())))
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(response, Project.class);
        Long projectId = createdProject.getId();

        // Test get by ID
        mockMvc.perform(get("/api/projects/" + projectId)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is(project.getName())));

        // Test get all projects
        mockMvc.perform(get("/api/projects")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(project.getName())));

        // Test get featured projects
        mockMvc.perform(get("/api/projects/featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].featured", is(true)));

        // Test get projects by startup
        mockMvc.perform(get("/api/projects/startup/" + testStartupId)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startupId", is(testStartupId.intValue())));

        // Test get projects by status
        mockMvc.perform(get("/api/projects/status/In%20Progress")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("In Progress")));
    }

    @Test
    public void testUpdateProject() throws Exception {
        // Create a test project first
        Project project = new Project();
        project.setName("Original Name");
        project.setStatus("Planned");
        project.setFeatured(false);
        
        MvcResult createResult = mockMvc.perform(post("/api/projects?startupId=" + testStartupId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Project.class);
        
        // Update the project
        Project updatedProject = new Project();
        updatedProject.setName("Updated Name");
        updatedProject.setStatus("In Progress");
        updatedProject.setTechnologies("Java, Spring Boot, React, PostgreSQL");
        updatedProject.setFeatured(true);
        updatedProject.setStartDate(LocalDate.now());
        updatedProject.setTargetCompletionDate(LocalDate.now().plusMonths(3));
        
        mockMvc.perform(put("/api/projects/" + createdProject.getId())
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.status", is("In Progress")))
                .andExpect(jsonPath("$.featured", is(true)));
    }

    @Test
    public void testDeleteProject() throws Exception {
        // Create a test project first
        Project project = new Project();
        project.setName("Project to delete");
        project.setStatus("Planned");
        
        MvcResult createResult = mockMvc.perform(post("/api/projects?startupId=" + testStartupId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Project.class);
        
        // Delete the project
        mockMvc.perform(delete("/api/projects/" + createdProject.getId())
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
        
        // Verify the project is deleted
        mockMvc.perform(get("/api/projects/" + createdProject.getId())
                        .header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testToggleFeaturedStatus() throws Exception {
        // Create a test project first
        Project project = new Project();
        project.setName("Test Toggle");
        project.setStatus("In Progress");
        project.setFeatured(false);
        
        MvcResult createResult = mockMvc.perform(post("/api/projects?startupId=" + testStartupId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Project.class);
        
        // Toggle featured status
        mockMvc.perform(patch("/api/projects/" + createdProject.getId() + "/toggle-featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featured", is(true)));
        
        // Toggle back
        mockMvc.perform(patch("/api/projects/" + createdProject.getId() + "/toggle-featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featured", is(false)));
    }

    @Test
    public void testProjectWithMultipleTechnologies() throws Exception {
        // Create a project with multiple technologies
        Project project = new Project();
        project.setName("Multi-tech Project");
        project.setStatus("In Progress");
        project.setTechnologies("Java, Spring Boot, React, PostgreSQL, Docker");
        
        mockMvc.perform(post("/api/projects?startupId=" + testStartupId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.technologies", is("Java, Spring Boot, React, PostgreSQL, Docker")));
    }
}
