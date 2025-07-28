package com.kpt.incubation.backend.controller;

import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminService adminService;

    @Autowired
    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = adminService.authenticateAdmin(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> register(@Valid @RequestBody Admin admin) {
        Admin newAdmin = adminService.registerAdmin(admin);
        return ResponseEntity.ok(newAdmin);
    }
}
