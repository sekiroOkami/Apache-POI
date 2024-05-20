package com.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ImageProcessorTest {
    public ImageProcessor imageProcessor;

    @BeforeEach
    void setUp() {
        imageProcessor = new ImageProcessor();
    }

    @Test
    @DisplayName("Test loadImages()")
    void shouldNotReturnNull(@TempDir Path tempDir) throws IOException {
        List<Path> paths = imageProcessor.loadImages(tempDir);
        assertNotNull(paths);
    }

    @Test
    @DisplayName("Test loadImages() with non-empty directory")
    void shouldReturnListOfImages(@TempDir Path tempDir) throws IOException{
        // create some image files in the temp directory
        Files.createFile(tempDir.resolve("image1.jpg"));
        Files.createFile(tempDir.resolve("image2.png"));
        Files.createFile(tempDir.resolve("image3.jpeg"));
        // create a non-image file
        Files.createFile(tempDir.resolve("document.txt"));
        List<Path> paths = imageProcessor.loadImages(tempDir);
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
        assertEquals(3,paths.size());
    }

}
