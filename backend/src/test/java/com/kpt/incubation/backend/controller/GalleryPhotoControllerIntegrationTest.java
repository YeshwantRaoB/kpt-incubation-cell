package com.kpt.incubation.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.entity.GalleryPhoto;
import com.kpt.incubation.backend.repository.AdminRepository;
import com.kpt.incubation.backend.repository.GalleryPhotoRepository;
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
public class GalleryPhotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private GalleryPhotoRepository galleryPhotoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private final String TEST_EMAIL = "gallerytest@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() throws Exception {
        // Clean up test data
        galleryPhotoRepository.deleteAll();
        adminRepository.deleteAll();

        // Create a test admin
        Admin admin = new Admin();
        admin.setEmail(TEST_EMAIL);
        admin.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        admin.setName("Gallery Test Admin");
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
    public void testCreateAndGetPhoto() throws Exception {
        // Create a new photo
        GalleryPhoto photo = new GalleryPhoto();
        photo.setTitle("Test Photo");
        photo.setDescription("This is a test photo");
        photo.setImageUrl("https://example.com/test.jpg");
        photo.setAltText("Test alt text");
        photo.setFeatured(true);

        // Test create
        MvcResult createResult = mockMvc.perform(post("/api/gallery")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(photo.getTitle())))
                .andExpect(jsonPath("$.featured", is(true)))
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        GalleryPhoto createdPhoto = objectMapper.readValue(response, GalleryPhoto.class);
        Long photoId = createdPhoto.getId();

        // Test get by ID
        mockMvc.perform(get("/api/gallery/" + photoId)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(photoId.intValue())))
                .andExpect(jsonPath("$.title", is(photo.getTitle())));

        // Test get all photos
        mockMvc.perform(get("/api/gallery")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(photo.getTitle())));

        // Test get featured photos
        mockMvc.perform(get("/api/gallery/featured")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].featured", is(true)));
    }

    @Test
    public void testUpdatePhoto() throws Exception {
        // Create a test photo first
        GalleryPhoto photo = new GalleryPhoto();
        photo.setTitle("Original Title");
        photo.setImageUrl("https://example.com/original.jpg");
        photo.setFeatured(false);
        
        MvcResult createResult = mockMvc.perform(post("/api/gallery")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isOk())
                .andReturn();

        GalleryPhoto createdPhoto = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), GalleryPhoto.class);
        
        // Update the photo
        GalleryPhoto updatedPhoto = new GalleryPhoto();
        updatedPhoto.setTitle("Updated Title");
        updatedPhoto.setFeatured(true);
        
        mockMvc.perform(put("/api/gallery/" + createdPhoto.getId())
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPhoto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.featured", is(true)));
    }

    @Test
    public void testDeletePhoto() throws Exception {
        // Create a test photo first
        GalleryPhoto photo = new GalleryPhoto();
        photo.setTitle("Photo to delete");
        photo.setImageUrl("https://example.com/delete.jpg");
        
        MvcResult createResult = mockMvc.perform(post("/api/gallery")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isOk())
                .andReturn();

        GalleryPhoto createdPhoto = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), GalleryPhoto.class);
        
        // Delete the photo
        mockMvc.perform(delete("/api/gallery/" + createdPhoto.getId())
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
        
        // Verify the photo is deleted
        mockMvc.perform(get("/api/gallery/" + createdPhoto.getId())
                        .header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReorderPhotos() throws Exception {
        // Create test photos
        GalleryPhoto photo1 = createTestPhoto("Photo 1", 1);
        GalleryPhoto photo2 = createTestPhoto("Photo 2", 2);
        
        // Reorder photos (swap positions)
        List<Long> newOrder = Arrays.asList(photo2.getId(), photo1.getId());
        
        mockMvc.perform(post("/api/gallery/reorder")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk());
        
        // Verify new order
        mockMvc.perform(get("/api/gallery")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Photo 2")))
                .andExpect(jsonPath("$[1].title", is("Photo 1")));
    }

    private GalleryPhoto createTestPhoto(String title, int displayOrder) throws Exception {
        GalleryPhoto photo = new GalleryPhoto();
        photo.setTitle(title);
        photo.setImageUrl("https://example.com/" + title.toLowerCase().replace(" ", "") + ".jpg");
        photo.setDisplayOrder(displayOrder);
        
        MvcResult result = mockMvc.perform(post("/api/gallery")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isOk())
                .andReturn();
        
        return objectMapper.readValue(result.getResponse().getContentAsString(), GalleryPhoto.class);
    }
}
