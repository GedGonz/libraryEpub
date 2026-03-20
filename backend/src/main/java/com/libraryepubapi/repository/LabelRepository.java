package com.libraryepubapi.repository;

import com.libraryepubapi.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabelRepository extends JpaRepository<Label, Integer> {

    Page<Label> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
}

