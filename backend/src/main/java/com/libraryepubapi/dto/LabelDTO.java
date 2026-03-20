package com.libraryepubapi.dto;

public class LabelDTO {
    private Integer labelId;
    private String name;

    public LabelDTO(Integer labelId, String name) {
        this.labelId = labelId;
        this.name = name;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public String getName() {
        return name;
    }
}

