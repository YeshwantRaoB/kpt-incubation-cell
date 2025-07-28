package com.kpt.incubation.backend.service;

import com.kpt.incubation.backend.dto.AuthRequest;
import com.kpt.incubation.backend.dto.AuthResponse;
import com.kpt.incubation.backend.entity.Admin;
import com.kpt.incubation.backend.exception.ResourceAlreadyExistsException;
import com.kpt.incubation.backend.exception.ResourceNotFoundException;
import com.kpt.incubation.backend.repository.AdminRepository;
import com.kpt.incubation.backend.security.AdminDetailsImpl;
import com.kpt.incubation.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticateAdmin(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        AdminDetailsImpl adminDetails = (AdminDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(adminDetails);
        
        return new AuthResponse(jwt, adminDetails.getUsername(), adminDetails.getName(), "Login successful");
    }

    public Admin registerAdmin(Admin admin) {
        // Check if email already exists
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use: " + admin.getEmail());
        }
        
        // Encode password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setActive(true);
        
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = getAdminById(id);
        
        // Check if the new email is already in use by another admin
        if (adminRepository.existsByEmail(adminDetails.getEmail()) && 
            !admin.getEmail().equals(adminDetails.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use: " + adminDetails.getEmail());
        }
        
        admin.setName(adminDetails.getName());
        admin.setEmail(adminDetails.getEmail());
        
        // Only update password if a new one is provided
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        }
        
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        Admin admin = getAdminById(id);
        admin.setActive(false);
        adminRepository.save(admin);
    }
    
    public void hardDeleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }
}
