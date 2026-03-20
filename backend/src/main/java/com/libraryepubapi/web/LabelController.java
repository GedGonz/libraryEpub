package com.libraryepubapi.web;

import com.libraryepubapi.dto.LabelDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.service.LabelService;
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
@RequestMapping("/api/labels")
@Validated
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public PageResponse<LabelDTO> listLabels(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return labelService.listLabels(page, size);
    }

    @GetMapping("/{labelId}")
    public LabelDTO getLabelById(
            @PathVariable @Min(1) int labelId
    ) {
        return labelService.getLabelById(labelId);
    }

    @GetMapping("/search")
    public PageResponse<LabelDTO> searchLabelsByPrefix(
            @RequestParam @NotBlank @Pattern(regexp = "^[A-Za-z]$") String prefix,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return labelService.searchLabelsByPrefix(prefix, page, size);
    }
}

