package com.libraryepubapi.service;

import com.libraryepubapi.dto.LabelDTO;
import com.libraryepubapi.dto.PageResponse;
import com.libraryepubapi.entity.Label;
import com.libraryepubapi.repository.LabelRepository;
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
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<LabelDTO> listLabels(int page, int size) {
        Pageable pageable = toPageable(page, size);
        Page<Label> result = labelRepository.findAll(pageable);
        List<LabelDTO> items = result.getContent()
                .stream()
                .map(l -> new LabelDTO(l.getLabelId(), l.getName()))
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    @Transactional(readOnly = true)
    public LabelDTO getLabelById(int labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found: " + labelId));
        return new LabelDTO(label.getLabelId(), label.getName());
    }

    @Transactional(readOnly = true)
    public PageResponse<LabelDTO> searchLabelsByPrefix(String prefix, int page, int size) {
        String safePrefix = prefix == null ? "" : prefix.trim();
        Pageable pageable = toPageable(page, size);
        Page<Label> result = labelRepository.findByNameStartingWithIgnoreCase(safePrefix, pageable);
        List<LabelDTO> items = result.getContent()
                .stream()
                .map(l -> new LabelDTO(l.getLabelId(), l.getName()))
                .collect(Collectors.toList());
        return new PageResponse<>(page, size, result.getTotalElements(), result.getTotalPages(), items);
    }

    private Pageable toPageable(int page, int size) {
        int safePage = Math.max(page, 1);
        int zeroBasedPage = safePage - 1;
        return PageRequest.of(zeroBasedPage, size, Sort.by("labelId").ascending());
    }
}

