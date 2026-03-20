package com.libraryepubapi.service;

import com.libraryepubapi.dto.AuthorDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.entity.Author;
import com.libraryepubapi.repository.AuthorRepository;
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
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<AuthorDTO> listAuthors(int page, int size) {
        Pageable pageable = toPageable(page, size);
        Page<Author> result = authorRepository.findAll(pageable);
        List<AuthorDTO> items = result.getContent()
                .stream()
                .map(a -> new AuthorDTO(a.getAuthorId(), a.getName()))
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(int authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + authorId));
        return new AuthorDTO(author.getAuthorId(), author.getName());
    }

    @Transactional(readOnly = true)
    public PageResponse<AuthorDTO> searchAuthorsByPrefix(String prefix, int page, int size) {
        String safePrefix = prefix == null ? "" : prefix.trim();
        Pageable pageable = toPageable(page, size);
        Page<Author> result = authorRepository.findByNameStartingWithIgnoreCase(safePrefix, pageable);
        List<AuthorDTO> items = result.getContent()
                .stream()
                .map(a -> new AuthorDTO(a.getAuthorId(), a.getName()))
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    private Pageable toPageable(int page, int size) {
        int safePage = Math.max(page, 1);
        int zeroBasedPage = safePage - 1;
        return PageRequest.of(zeroBasedPage, size, Sort.by("authorId").ascending());
    }
}

