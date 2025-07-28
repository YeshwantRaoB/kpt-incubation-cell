package com.kpt.incubation.backend.service;

import com.kpt.incubation.backend.entity.GalleryPhoto;
import com.kpt.incubation.backend.exception.ResourceNotFoundException;
import com.kpt.incubation.backend.repository.GalleryPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryPhotoService {

    private final GalleryPhotoRepository galleryPhotoRepository;

    @Autowired
    public GalleryPhotoService(GalleryPhotoRepository galleryPhotoRepository) {
        this.galleryPhotoRepository = galleryPhotoRepository;
    }

    public List<GalleryPhoto> getAllPhotos() {
        return galleryPhotoRepository.findAllByOrderByDisplayOrderAsc();
    }

    public List<GalleryPhoto> getFeaturedPhotos() {
        return galleryPhotoRepository.findByIsFeaturedTrueOrderByDisplayOrderAsc();
    }

    public GalleryPhoto getPhotoById(Long id) {
        return galleryPhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));
    }

    public GalleryPhoto createPhoto(GalleryPhoto photo) {
        // Set default display order to be the next in sequence
        if (photo.getDisplayOrder() == null) {
            Long maxDisplayOrder = galleryPhotoRepository.findMaxDisplayOrder()
                    .orElse(0L);
            photo.setDisplayOrder(Math.toIntExact(maxDisplayOrder + 1));
        }
        
        return galleryPhotoRepository.save(photo);
    }

    public GalleryPhoto updatePhoto(Long id, GalleryPhoto photoDetails) {
        GalleryPhoto photo = getPhotoById(id);
        
        photo.setTitle(photoDetails.getTitle());
        photo.setDescription(photoDetails.getDescription());
        photo.setImageUrl(photoDetails.getImageUrl());
        photo.setAltText(photoDetails.getAltText());
        photo.setFeatured(photoDetails.isFeatured());
        
        // Only update display order if explicitly set
        if (photoDetails.getDisplayOrder() != null) {
            photo.setDisplayOrder(photoDetails.getDisplayOrder());
        }
        
        return galleryPhotoRepository.save(photo);
    }

    public void deletePhoto(Long id) {
        GalleryPhoto photo = getPhotoById(id);
        galleryPhotoRepository.delete(photo);
    }

    public void reorderPhotos(List<Long> photoIdsInOrder) {
        for (int i = 0; i < photoIdsInOrder.size(); i++) {
            Long photoId = photoIdsInOrder.get(i);
            GalleryPhoto photo = getPhotoById(photoId);
            photo.setDisplayOrder(i + 1); // 1-based index for display order
            galleryPhotoRepository.save(photo);
        }
    }
}
