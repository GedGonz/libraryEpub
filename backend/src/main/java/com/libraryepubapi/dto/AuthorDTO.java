package com.libraryepubapi.dto;

public class AuthorDTO {
    private Integer authorId;
    private String name;

    public AuthorDTO(Integer authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }
}

