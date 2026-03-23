package com.libraryepubapi.entity;

import java.io.Serializable;
import java.util.Objects;

public class DeviceFavoriteId implements Serializable {
    private String clientId;
    private Integer bookId;

    public DeviceFavoriteId() {
    }

    public DeviceFavoriteId(String clientId, Integer bookId) {
        this.clientId = clientId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceFavoriteId that)) return false;
        return Objects.equals(clientId, that.clientId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, bookId);
    }
}

