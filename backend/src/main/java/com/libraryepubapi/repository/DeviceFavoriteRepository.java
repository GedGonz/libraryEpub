package com.libraryepubapi.repository;

import com.libraryepubapi.entity.DeviceFavorite;
import com.libraryepubapi.entity.DeviceFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceFavoriteRepository extends JpaRepository<DeviceFavorite, DeviceFavoriteId> {
    List<DeviceFavorite> findAllByClientIdOrderByCreatedAtDesc(String clientId);

    boolean existsByClientIdAndBookId(String clientId, Integer bookId);

    void deleteByClientIdAndBookId(String clientId, Integer bookId);
}

