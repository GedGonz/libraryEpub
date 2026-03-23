package com.libraryepubapi.service;

import com.libraryepubapi.dto.BookListItemDTO;
import com.libraryepubapi.entity.Author;
import com.libraryepubapi.entity.Book;
import com.libraryepubapi.entity.DeviceFavorite;
import com.libraryepubapi.repository.BookRepository;
import com.libraryepubapi.repository.DeviceFavoriteRepository;
import com.libraryepubapi.web.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {
    private final DeviceFavoriteRepository deviceFavoriteRepository;
    private final BookRepository bookRepository;

    public FavoriteService(DeviceFavoriteRepository deviceFavoriteRepository, BookRepository bookRepository) {
        this.deviceFavoriteRepository = deviceFavoriteRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookListItemDTO> listFavorites(String clientId) {
        validateClientId(clientId);
        List<DeviceFavorite> favorites = deviceFavoriteRepository.findAllByClientIdOrderByCreatedAtDesc(clientId);
        if (favorites.isEmpty()) {
            return List.of();
        }

        List<Long> bookIds = favorites.stream().map(f -> f.getBookId().longValue()).toList();
        List<Book> books = bookRepository.findAllById(bookIds);
        Map<Long, Book> booksById = new HashMap<>();
        for (Book book : books) {
            booksById.put(book.getBookId(), book);
        }

        return bookIds.stream()
                .map(booksById::get)
                .filter(book -> book != null)
                .map(this::toListItemDTO)
                .toList();
    }

    @Transactional
    public void addFavorite(String clientId, long bookId) {
        validateClientId(clientId);
        if (bookId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Book id is out of supported range");
        }

        int normalizedBookId = (int) bookId;
        if (deviceFavoriteRepository.existsByClientIdAndBookId(clientId, normalizedBookId)) {
            return;
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        DeviceFavorite favorite = new DeviceFavorite(clientId, book.getBookId().intValue(), LocalDateTime.now());
        deviceFavoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(String clientId, long bookId) {
        validateClientId(clientId);
        if (bookId > Integer.MAX_VALUE) {
            return;
        }
        deviceFavoriteRepository.deleteByClientIdAndBookId(clientId, (int) bookId);
    }

    private void validateClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("X-Client-Id header is required");
        }
        if (!clientId.matches("^[a-zA-Z0-9-]{8,64}$")) {
            throw new IllegalArgumentException("X-Client-Id must be alphanumeric UUID-like value");
        }
    }

    private BookListItemDTO toListItemDTO(Book b) {
        List<String> authorNames = b.getAuthors()
                .stream()
                .map(Author::getName)
                .sorted()
                .toList();

        return new BookListItemDTO(
                b.getBookId(),
                b.getTitle(),
                b.getDescription(),
                b.getPublishedYear(),
                b.getPageCount(),
                b.getSha256sum(),
                b.getFileSize(),
                b.getFileName(),
                authorNames
        );
    }
}

