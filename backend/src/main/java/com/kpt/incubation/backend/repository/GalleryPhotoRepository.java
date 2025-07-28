package com.kpt.incubation.backend.repository;

import com.kpt.incubation.backend.entity.GalleryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryPhotoRepository extends JpaRepository<GalleryPhoto, Long> {
    List<GalleryPhoto> findAllByOrderByDisplayOrderAsc();
    List<GalleryPhoto> findByIsFeaturedTrueOrderByDisplayOrderAsc();
}
