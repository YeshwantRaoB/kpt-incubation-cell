package com.kpt.incubation.backend.controller;

import com.kpt.incubation.backend.entity.Startup;
import com.kpt.incubation.backend.service.StartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

    private final StartupService startupService;

    @Autowired
    public StartupController(StartupService startupService) {
        this.startupService = startupService;
    }

    @GetMapping
    public ResponseEntity<List<Startup>> getAllStartups() {
        return ResponseEntity.ok(startupService.getAllStartups());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Startup>> getFeaturedStartups() {
        return ResponseEntity.ok(startupService.getFeaturedStartups());
    }

    @GetMapping("/industry/{industry}")
    public ResponseEntity<List<Startup>> getStartupsByIndustry(@PathVariable String industry) {
        return ResponseEntity.ok(startupService.getStartupsByIndustry(industry));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Startup> getStartupById(@PathVariable Long id) {
        return ResponseEntity.ok(startupService.getStartupById(id));
    }

    @PostMapping
    public ResponseEntity<Startup> createStartup(@RequestBody Startup startup) {
        return ResponseEntity.ok(startupService.createStartup(startup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Startup> updateStartup(
            @PathVariable Long id, 
            @RequestBody Startup startupDetails) {
        return ResponseEntity.ok(startupService.updateStartup(id, startupDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStartup(@PathVariable Long id) {
        startupService.deleteStartup(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<Startup> toggleFeaturedStatus(@PathVariable Long id) {
        startupService.toggleFeaturedStatus(id);
        return ResponseEntity.ok(startupService.getStartupById(id));
    }
}
