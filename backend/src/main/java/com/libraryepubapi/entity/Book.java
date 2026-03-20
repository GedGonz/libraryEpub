package com.libraryepubapi.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "sha256sum", length = 64, unique = true)
    private String sha256sum;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_name")
    private String fileName;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_labels",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();

    public Long getBookId() {
        return bookId;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public String getSha256sum() {
        return sha256sum;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Set<Label> getLabels() {
        return labels;
    }
}

