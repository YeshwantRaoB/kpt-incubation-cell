package com.kpt.incubation.backend.controller;

import com.kpt.incubation.backend.entity.GalleryPhoto;
import com.kpt.incubation.backend.service.GalleryPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryPhotoController {

    private final GalleryPhotoService galleryPhotoService;

    @Autowired
    public GalleryPhotoController(GalleryPhotoService galleryPhotoService) {
        this.galleryPhotoService = galleryPhotoService;
    }

    @GetMapping
    public ResponseEntity<List<GalleryPhoto>> getAllPhotos() {
        return ResponseEntity.ok(galleryPhotoService.getAllPhotos());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<GalleryPhoto>> getFeaturedPhotos() {
        return ResponseEntity.ok(galleryPhotoService.getFeaturedPhotos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryPhoto> getPhotoById(@PathVariable Long id) {
        return ResponseEntity.ok(galleryPhotoService.getPhotoById(id));
    }

    @PostMapping
    public ResponseEntity<GalleryPhoto> createPhoto(@RequestBody GalleryPhoto photo) {
        return ResponseEntity.ok(galleryPhotoService.createPhoto(photo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryPhoto> updatePhoto(
            @PathVariable Long id, 
            @RequestBody GalleryPhoto photoDetails) {
        return ResponseEntity.ok(galleryPhotoService.updatePhoto(id, photoDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        galleryPhotoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reorder")
    public ResponseEntity<Void> reorderPhotos(@RequestBody List<Long> photoIdsInOrder) {
        galleryPhotoService.reorderPhotos(photoIdsInOrder);
        return ResponseEntity.ok().build();
    }
}
