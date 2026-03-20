package com.libraryepubapi.service;

import com.libraryepubapi.web.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EpubFileService {
    private final Path storageBasePath;

    public EpubFileService(@Value("${EPUB_STORAGE_BASE_PATH:}") String epubStorageBasePath) {
        if (epubStorageBasePath == null || epubStorageBasePath.isBlank()) {
            // No lanzamos aquí para no romper el arranque sin ese env var;
            // el endpoint validará al momento de servir.
            this.storageBasePath = null;
            return;
        }
        this.storageBasePath = Paths.get(epubStorageBasePath);
    }

    public Resource loadEpubResource(String fileName) {
        if (storageBasePath == null) {
            throw new IllegalStateException("EPUB_STORAGE_BASE_PATH env var no está configurado");
        }
        if (fileName == null || fileName.isBlank()) {
            throw new ResourceNotFoundException("EPUB fileName vacío");
        }

        Path resolvedPath = resolveSafePath(fileName);
        if (!Files.exists(resolvedPath) || !Files.isReadable(resolvedPath)) {
            throw new ResourceNotFoundException("EPUB no encontrado: " + resolvedPath);
        }

        try {
            return new UrlResource(resolvedPath.toUri());
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("No se pudo acceder al EPUB: " + e.getMessage());
        }
    }

    private Path resolveSafePath(String fileName) {
        // Normalizamos separadores y quitamos cualquier path absoluto en el input
        // para que siempre resolvamos dentro del base path.
        String normalizedInput = fileName.replace('\\', '/').trim();
        while (normalizedInput.startsWith("/")) {
            normalizedInput = normalizedInput.substring(1);
        }

        Path inputPath = Paths.get(normalizedInput).normalize();
        if (inputPath.startsWith("..")) {
            throw new IllegalArgumentException("Invalid EPUB fileName (path traversal)");
        }

        // Aseguramos extensión .epub.
        String leaf = inputPath.getFileName().toString();
        if (!leaf.toLowerCase().endsWith(".epub")) {
            Path parent = inputPath.getParent();
            leaf = leaf + ".epub";
            inputPath = parent == null ? Paths.get(leaf) : parent.resolve(leaf);
        }

        Path baseNormalized = storageBasePath.normalize();
        Path resolved = baseNormalized.resolve(inputPath).normalize();

        if (!resolved.startsWith(baseNormalized)) {
            throw new IllegalArgumentException("Invalid EPUB fileName (outside base path)");
        }

        return resolved;
    }
}

