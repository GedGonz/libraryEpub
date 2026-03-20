package com.libraryepubapi.dto;

import java.util.List;

public class BookDetailDTO {
    private Long bookId;
    private String title;
    private String description;
    private Integer publishedYear;
    private Integer pageCount;
    private String sha256sum;
    private Long fileSize;
    private String fileName;

    private List<Integer> authorIds;
    private List<Integer> labelIds;

    public BookDetailDTO(Long bookId, String title, String description, Integer publishedYear, Integer pageCount,
                           String sha256sum, Long fileSize, String fileName,
                           List<Integer> authorIds, List<Integer> labelIds) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.publishedYear = publishedYear;
        this.pageCount = pageCount;
        this.sha256sum = sha256sum;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.authorIds = authorIds;
        this.labelIds = labelIds;
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

    public List<Integer> getAuthorIds() {
        return authorIds;
    }

    public List<Integer> getLabelIds() {
        return labelIds;
    }
}

