package com.libraryepubapi.repository;

import com.libraryepubapi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Page<Author> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
}

