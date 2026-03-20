package com.libraryepubapi.web;

import com.libraryepubapi.dto.BookDetailDTO;
import com.libraryepubapi.dto.BookListItemDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.service.EpubFileService;
import com.libraryepubapi.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {
    private final BookService bookService;
    private final EpubFileService epubFileService;

    public BookController(BookService bookService, EpubFileService epubFileService) {
        this.bookService = bookService;
        this.epubFileService = epubFileService;
    }

    @GetMapping
    public PageResponse<BookListItemDTO> listBooks(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return bookService.listBooks(page, size);
    }

    @GetMapping("/search")
    public PageResponse<BookListItemDTO> searchBooks(
            @RequestParam @NotBlank String q,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return bookService.searchBooks(q, page, size);
    }

    @GetMapping("/{bookId}")
    public BookDetailDTO getBookById(
            @PathVariable @Min(1) long bookId
    ) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/{bookId}/file")
    public ResponseEntity<Resource> getBookFile(
            @PathVariable @Min(1) long bookId
    ) {
        String fileName = bookService.getFileNameByBookId(bookId);
        Resource resource = epubFileService.loadEpubResource(fileName);

        String filename = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/epub+zip"));
        if (filename != null && !filename.isBlank()) {
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/by-title-prefix/{prefix}")
    public PageResponse<BookListItemDTO> listBooksByTitlePrefix(
            @PathVariable @NotBlank @Pattern(regexp = "^[A-Za-z]$") String prefix,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return bookService.listBooksByTitlePrefix(prefix, page, size);
    }

    @GetMapping("/by-author/{authorId}")
    public PageResponse<BookListItemDTO> listBooksByAuthor(
            @PathVariable @Min(1) int authorId,
            @RequestParam(required = false) String prefix,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return bookService.listBooksByAuthor(authorId, prefix, page, size);
    }

    @GetMapping("/by-label/{labelId}")
    public PageResponse<BookListItemDTO> listBooksByLabel(
            @PathVariable @Min(1) int labelId,
            @RequestParam(required = false) String prefix,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return bookService.listBooksByLabel(labelId, prefix, page, size);
    }
}

