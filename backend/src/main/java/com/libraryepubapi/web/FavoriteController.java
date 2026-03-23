package com.libraryepubapi.web;

import com.libraryepubapi.dto.BookListItemDTO;
import com.libraryepubapi.service.FavoriteService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@Validated
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<BookListItemDTO> listFavorites(
            @RequestHeader("X-Client-Id") String clientId
    ) {
        return favoriteService.listFavorites(clientId);
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable @Min(1) long bookId
    ) {
        favoriteService.addFavorite(clientId, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable @Min(1) long bookId
    ) {
        favoriteService.removeFavorite(clientId, bookId);
        return ResponseEntity.noContent().build();
    }
}

