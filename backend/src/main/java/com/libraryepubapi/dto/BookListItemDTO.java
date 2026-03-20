package com.libraryepubapi.dto;

import java.util.List;

public class BookListItemDTO {
    private Long bookId;
    private String title;
    private String description;
    private Integer publishedYear;
    private Integer pageCount;
    private String sha256sum;
    private Long fileSize;
    private String fileName;
    private List<String> authorNames;

    public BookListItemDTO(Long bookId, String title, String description, Integer publishedYear, Integer pageCount,
                              String sha256sum, Long fileSize, String fileName, List<String> authorNames) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.publishedYear = publishedYear;
        this.pageCount = pageCount;
        this.sha256sum = sha256sum;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.authorNames = authorNames;
    }

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

    public List<String> getAuthorNames() {
        return authorNames;
    }
}

