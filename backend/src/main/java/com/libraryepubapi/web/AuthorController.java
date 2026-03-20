package com.libraryepubapi.web;

import com.libraryepubapi.dto.AuthorDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.service.AuthorService;
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
@RequestMapping("/api/authors")
@Validated
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public PageResponse<AuthorDTO> listAuthors(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return authorService.listAuthors(page, size);
    }

    @GetMapping("/{authorId}")
    public AuthorDTO getAuthorById(
            @PathVariable @Min(1) int authorId
    ) {
        return authorService.getAuthorById(authorId);
    }

    @GetMapping("/search")
    public PageResponse<AuthorDTO> searchAuthorsByPrefix(
            @RequestParam @NotBlank @Pattern(regexp = "^[A-Za-z]$") String prefix,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return authorService.searchAuthorsByPrefix(prefix, page, size);
    }
}

