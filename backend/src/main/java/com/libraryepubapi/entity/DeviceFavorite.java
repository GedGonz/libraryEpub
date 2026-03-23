package com.libraryepubapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_favorites")
@IdClass(DeviceFavoriteId.class)
public class DeviceFavorite {
    @Id
    @Column(name = "client_id", length = 64, nullable = false)
    private String clientId;

    @Id
    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public DeviceFavorite() {
    }

    public DeviceFavorite(String clientId, Integer bookId, LocalDateTime createdAt) {
        this.clientId = clientId;
        this.bookId = bookId;
        this.createdAt = createdAt;
    }

    public String getClientId() {
        return clientId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

