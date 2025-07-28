package com.kpt.incubation.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.entity.Startup;
import com.kpt.incubation.backend.repository.AdminRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StartupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private final String TEST_EMAIL = "startuptest@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() throws Exception {
        // Clean up test data
        startupRepository.deleteAll();
        adminRepository.deleteAll();

        // Create a test admin
        Admin admin = new Admin();
        admin.setEmail(TEST_EMAIL);
        admin.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        admin.setName("Startup Test Admin");
        admin.setActive(true);
        adminRepository.save(admin);

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
    public void testCreateAndGetStartup() throws Exception {
        // Create a new startup
        Startup startup = new Startup();
        startup.setName("Test Startup");
        startup.setDescription("This is a test startup");
        startup.setIndustry("Technology");
        startup.setFounderName("John Doe");
        startup.setWebsiteUrl("https://teststartup.example.com");
        startup.setFeatured(true);

        // Test create
        MvcResult createResult = mockMvc.perform(post("/api/startups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(startup.getName())))
                .andExpect(jsonPath("$.featured", is(true)))
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        Startup createdStartup = objectMapper.readValue(response, Startup.class);
        Long startupId = createdStartup.getId();

        // Test get by ID
        mockMvc.perform(get("/api/startups/" + startupId)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(startupId.intValue())))
                .andExpect(jsonPath("$.name", is(startup.getName())));

        // Test get all startups
        mockMvc.perform(get("/api/startups")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(startup.getName())));

        // Test get featured startups
        mockMvc.perform(get("/api/startups/featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].featured", is(true)));

        // Test get by industry
        mockMvc.perform(get("/api/startups/industry/Technology")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].industry", is("Technology")));
    }

    @Test
    public void testUpdateStartup() throws Exception {
        // Create a test startup first
        Startup startup = new Startup();
        startup.setName("Original Name");
        startup.setIndustry("Original Industry");
        startup.setFeatured(false);
        
        MvcResult createResult = mockMvc.perform(post("/api/startups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startup)))
                .andExpect(status().isOk())
                .andReturn();

        Startup createdStartup = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Startup.class);
        
        // Update the startup
        Startup updatedStartup = new Startup();
        updatedStartup.setName("Updated Name");
        updatedStartup.setIndustry("Updated Industry");
        updatedStartup.setFeatured(true);
        
        mockMvc.perform(put("/api/startups/" + createdStartup.getId())
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStartup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.industry", is("Updated Industry")))
                .andExpect(jsonPath("$.featured", is(true)));
    }

    @Test
    public void testDeleteStartup() throws Exception {
        // Create a test startup first
        Startup startup = new Startup();
        startup.setName("Startup to delete");
        startup.setIndustry("Test Industry");
        
        MvcResult createResult = mockMvc.perform(post("/api/startups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startup)))
                .andExpect(status().isOk())
                .andReturn();

        Startup createdStartup = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Startup.class);
        
        // Delete the startup (soft delete)
        mockMvc.perform(delete("/api/startups/" + createdStartup.getId())
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
        
        // Verify the startup is not found in active list
        mockMvc.perform(get("/api/startups")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testToggleFeaturedStatus() throws Exception {
        // Create a test startup first
        Startup startup = new Startup();
        startup.setName("Test Toggle");
        startup.setIndustry("Test Industry");
        startup.setFeatured(false);
        
        MvcResult createResult = mockMvc.perform(post("/api/startups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startup)))
                .andExpect(status().isOk())
                .andReturn();

        Startup createdStartup = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Startup.class);
        
        // Toggle featured status
        mockMvc.perform(patch("/api/startups/" + createdStartup.getId() + "/toggle-featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featured", is(true)));
        
        // Toggle back
        mockMvc.perform(patch("/api/startups/" + createdStartup.getId() + "/toggle-featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featured", is(false)));
    }
}
