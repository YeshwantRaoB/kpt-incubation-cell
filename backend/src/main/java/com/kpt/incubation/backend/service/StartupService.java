package com.kpt.incubation.backend.service;

import com.kpt.incubation.backend.entity.Startup;
import com.kpt.incubation.backend.exception.ResourceAlreadyExistsException;
import com.kpt.incubation.backend.exception.ResourceNotFoundException;
import com.kpt.incubation.backend.repository.StartupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartupService {

    private final StartupRepository startupRepository;

    @Autowired
    public StartupService(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findByIsActiveTrueOrderByName();
    }

    public List<Startup> getFeaturedStartups() {
        return startupRepository.findByIsFeaturedTrueAndIsActiveTrue();
    }

    public List<Startup> getStartupsByIndustry(String industry) {
        return startupRepository.findByIndustryAndIsActiveTrue(industry);
    }

    public Startup getStartupById(Long id) {
        return startupRepository.findById(id)
                .filter(Startup::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Startup not found with id: " + id));
    }

    public Startup createStartup(Startup startup) {
        // Check if startup with the same name already exists
        if (startupRepository.existsByName(startup.getName())) {
            throw new ResourceAlreadyExistsException("Startup with name '" + startup.getName() + "' already exists");
        }
        
        startup.setActive(true);
        return startupRepository.save(startup);
    }

    public Startup updateStartup(Long id, Startup startupDetails) {
        Startup startup = getStartupById(id);
        
        // Check if the new name is already in use by another startup
        if (!startup.getName().equals(startupDetails.getName()) && 
            startupRepository.existsByName(startupDetails.getName())) {
            throw new ResourceAlreadyExistsException("Startup with name '" + startupDetails.getName() + "' already exists");
        }
        
        // Update all fields
        startup.setName(startupDetails.getName());
        startup.setDescription(startupDetails.getDescription());
        startup.setLogoUrl(startupDetails.getLogoUrl());
        startup.setWebsiteUrl(startupDetails.getWebsiteUrl());
        startup.setFounderName(startupDetails.getFounderName());
        startup.setFoundedYear(startupDetails.getFoundedYear());
        startup.setIndustry(startupDetails.getIndustry());
        startup.setFundingStage(startupDetails.getFundingStage());
        startup.setContactEmail(startupDetails.getContactEmail());
        startup.setFeatured(startupDetails.isFeatured());
        
        return startupRepository.save(startup);
    }

    public void deleteStartup(Long id) {
        Startup startup = getStartupById(id);
        startup.setActive(false);
        startupRepository.save(startup);
    }
    
    public void toggleFeaturedStatus(Long id) {
        Startup startup = getStartupById(id);
        startup.setFeatured(!startup.isFeatured());
        startupRepository.save(startup);
    }
}
