package com.libraryepubapi.service;

import com.libraryepubapi.dto.BookDetailDTO;
import com.libraryepubapi.dto.BookListItemDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.entity.Author;
import com.libraryepubapi.entity.Book;
import com.libraryepubapi.entity.Label;
import com.libraryepubapi.repository.BookRepository;
import com.libraryepubapi.web.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<BookListItemDTO> listBooks(int page, int size) {
        Pageable pageable = toPageable(page, size);
        Page<Book> result = bookRepository.findAll(pageable);
        List<BookListItemDTO> items = result.getContent()
                .stream()
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookListItemDTO> searchBooks(String q, int page, int size) {
        if (q == null || q.isBlank()) {
            throw new IllegalArgumentException("q must not be blank");
        }

        Pageable pageable = toPageable(page, size);
        Page<Book> result = bookRepository.searchByTitleOrAuthorName(q, pageable);
        List<BookListItemDTO> items = result.getContent()
                .stream()
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookListItemDTO> listBooksByTitlePrefix(String prefix, int page, int size) {
        String safePrefix = prefix == null ? "" : prefix.trim();
        Pageable pageable = toPageable(page, size);
        Page<Book> result = bookRepository.findByTitlePrefix(safePrefix, pageable);
        List<BookListItemDTO> items = result.getContent()
                .stream()
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookListItemDTO> listBooksByAuthor(int authorId, String prefix, int page, int size) {
        String safePrefix = prefix == null ? "" : prefix.trim();
        Pageable pageable = toPageable(page, size);
        Page<Book> result = bookRepository.findByAuthorIdAndTitlePrefix(authorId, safePrefix, pageable);
        List<BookListItemDTO> items = result.getContent()
                .stream()
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public PageResponse<BookListItemDTO> listBooksByLabel(int labelId, String prefix, int page, int size) {
        String safePrefix = prefix == null ? "" : prefix.trim();
        Pageable pageable = toPageable(page, size);
        Page<Book> result = bookRepository.findByLabelIdAndTitlePrefix(labelId, safePrefix, pageable);
        List<BookListItemDTO> items = result.getContent()
                .stream()
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public BookDetailDTO getBookById(long bookId) {
        Book book = bookRepository.findWithAuthorsAndLabelsByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        List<Integer> authorIds = book.getAuthors()
                .stream()
                .map(Author::getAuthorId)
                .collect(Collectors.toList());

        List<Integer> labelIds = book.getLabels()
                .stream()
                .map(Label::getLabelId)
                .collect(Collectors.toList());

        return new BookDetailDTO(
                book.getBookId(),
                book.getTitle(),
                book.getDescription(),
                book.getPublishedYear(),
                book.getPageCount(),
                book.getSha256sum(),
                book.getFileSize(),
                book.getFileName(),
                authorIds,
                labelIds
        );
    }

    @Transactional(readOnly = true)
    public String getFileNameByBookId(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
        return book.getFileName();
    }

    private Pageable toPageable(int page, int size) {
        int safePage = Math.max(page, 1);
        int zeroBasedPage = safePage - 1;
        return PageRequest.of(zeroBasedPage, size, Sort.by("bookId").ascending());
    }

    private BookListItemDTO toListItemDTO(Book b) {
        List<String> authorNames = b.getAuthors()
                .stream()
                .map(Author::getName)
                .sorted()
                .collect(Collectors.toList());

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

