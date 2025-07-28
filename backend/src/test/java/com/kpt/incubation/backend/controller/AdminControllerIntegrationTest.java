package com.kpt.incubation.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_NAME = "Test Admin";

    @BeforeEach
    public void setup() throws Exception {
        // Clean up test data
        adminRepository.deleteAll();

        // Create a test admin
        Admin admin = new Admin();
        admin.setEmail(TEST_EMAIL);
        admin.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        admin.setName(TEST_NAME);
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
    public void testAdminRegistration() throws Exception {
        AuthRequest newAdmin = new AuthRequest("newadmin@example.com", "newpassword123");
        newAdmin.setName("New Admin");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(newAdmin.getEmail())))
                .andExpect(jsonPath("$.name", is("New Admin")));
    }

    @Test
    public void testGetCurrentAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/me")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(TEST_EMAIL)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)));
    }

    @Test
    public void testUpdateAdmin() throws Exception {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setName("Updated Name");
        updatedAdmin.setPassword("newpassword123");

        mockMvc.perform(put("/api/admin/me")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    public void testDeleteAdmin() throws Exception {
        // First, create another admin to delete
        AuthRequest newAdmin = new AuthRequest("todelete@example.com", "password123");
        newAdmin.setName("To Delete");
        
        MvcResult createResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isCreated())
                .andReturn();

        // Now delete the admin
        mockMvc.perform(delete("/api/admin/me")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());

        // Verify the admin is deactivated (soft delete)
        mockMvc.perform(get("/api/admin/me")
                        .header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        // Try to access protected endpoint without token
        mockMvc.perform(get("/api/admin/me"))
                .andExpect(status().isForbidden());
    }
}
