package com.libraryepubapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Integer labelId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "labels")
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public Integer getLabelId() {
        return labelId;
    }

    public String getName() {
        return name;
    }
}

