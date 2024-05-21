package com.model;

import com.model.exception.ImageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImageProcessorTest {
    public ImageProcessor imageProcessor;

    @BeforeEach
    void setUp() {
        imageProcessor = new ImageProcessor();
    }

    @Test
    @DisplayName("Test loadImages()")
    void shouldNotReturnNull(@TempDir Path tempDir) throws IOException {
        var paths = imageProcessor.loadImages(tempDir);
        assertNotNull(paths);
    }

    @Test
    @DisplayName("Test loadImages():throw ImageProcessingException")
    void shouldThrowException(@TempDir Path tempDir) throws IOException {
        // Mock Files.list to throw IOException
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(
                    ()-> Files.list(any(Path.class))
            ).thenThrow(IOException.class);

            // Act & Assert
            assertThrows(
                    ImageProcessingException.class,
                    ()-> imageProcessor.loadImages(tempDir)
            );
        }
    }

    @Test
    @DisplayName("Test loadImages() with non-empty directory")
    void shouldReturnSetOfImages(@TempDir Path tempDir) throws IOException{
        // create some image files in the temp directory
        Files.createFile(tempDir.resolve("image1.jpg"));
        Files.createFile(tempDir.resolve("image2.png"));
        Files.createFile(tempDir.resolve("image3.jpeg"));
        // create a non-image file
        Files.createFile(tempDir.resolve("document.txt"));
        var paths = imageProcessor.loadImages(tempDir);
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
        assertEquals(3,paths.size());
    }

    @Test
    @DisplayName("Test loadImages() with no valid image filed")
    void shouldReturnEmptySetIfNoImages(@TempDir Path tempDir) throws IOException {
        // Create some non-image files
        Files.createFile(tempDir.resolve("document1.txt"));
        Files.createFile(tempDir.resolve("document2.txt"));
        var paths = imageProcessor.loadImages(tempDir);
        assertNotNull(paths);
        assertTrue(paths.isEmpty());
    }

    @Test
    @DisplayName("Test filterPortraitImages")
    void shouldReturnNonNull(@TempDir Path tempDir) throws IOException {
        // arrange : Create a valid portrait image in the temp directory
        Path imagePath = createTestImage(tempDir, "image1.png", 600, 800);
        var setOfPath = new HashSet<Path>();
        setOfPath.add(imagePath);
        // act
        Set<Path> paths = imageProcessor.filterPortraitImages(setOfPath);
        // assert
        assertNotNull(paths);
        assertTrue(paths.contains(imagePath));
    }

    @Test
    @DisplayName("Test filterPortraitImages")
    void shouldReturnSetOfPortraitImages(@TempDir Path tempDir) throws IOException {
        // arrange
        Path portraitImage1 = createTestImage(tempDir, "portrait1.jpg", 600, 800);
        Path portraitImage2 = createTestImage(tempDir, "portrait2.jpg", 720, 1080);
        Path landscapeImage1 = createTestImage(tempDir, "landscape1.jpg", 800, 600);
        Path landscapeImage2 = createTestImage(tempDir, "landscape2.jpg", 1080, 720);
        Set<Path> images = new HashSet<>(100, 0.75f);
        images.add(portraitImage1);
        images.add(portraitImage2);
        images.add(landscapeImage1);
        images.add(landscapeImage2);

        // act
        Set<Path> result = imageProcessor.filterPortraitImages(images);

        // assert
        assertAll(
                ()-> assertNotNull(result),
                ()-> assertEquals(2,result.size(), "Result size should be 2"),
                ()-> assertTrue(result.contains(portraitImage1), "Result should contain portraitImage1"),
                ()-> assertTrue(result.contains(portraitImage2), "Result should contain portraitImage2"),
                ()-> assertFalse(result.contains(landscapeImage1), "Result shouldn't contain landscapeImage1"),
                ()-> assertFalse(result.contains(landscapeImage2), "Result shouldn't contain landscapeImage2")
        );

    }

    @Test
    @DisplayName("test isPortrait: throw IOException")
    void shouldThrowIOException(@TempDir Path tempDir) {
        assertThrows(
                ImageProcessingException.class,
                () -> {
                    var samplePath = tempDir.resolve("");
                    imageProcessor.isPortrait(samplePath);
                }
        );
    }

    private Path createTestImage(Path directory, String fileName, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Path imagePath = directory.resolve(fileName);
        File imageFile = imagePath.toFile();
        ImageIO.write(image, "jpg", imageFile);
        return imagePath;
    }

    @Test
    @DisplayName("Test rotateImage")
    void shouldReturnRotateImage(@TempDir Path tempDir) throws IOException {
        Path path = createTestImage(tempDir, "Image1.png", 500, 1000);
        var image = ImageIO.read(path.toFile());
        BufferedImage rotatedImage = imageProcessor.rotateImage(image);

        assertNotNull(image, "Original image should not be null");
        assertNotNull(rotatedImage, "rotated image should not be null");
        assertEquals(image.getHeight(), rotatedImage.getWidth(),"Rotated image width should match original image height" );
        assertEquals(image.getWidth(), rotatedImage.getHeight(), "Rotated image height should match original image width");
        assertTrue(rotatedImage.getWidth() > rotatedImage.getHeight());

        // Optional: Check a few pixel values to ensure the rotation is correct
        // For example, top-left corner pixel of the original image should be at the bottom-left of the rotated image
        assertEquals(image.getRGB(0, 0), rotatedImage.getRGB(0, image.getWidth() - 1),
                "Top-left pixel of original should be bottom-left in rotated");
        assertEquals(image.getRGB(image.getWidth() - 1, image.getHeight() - 1), rotatedImage.getRGB(image.getHeight() - 1, 0),
                "Bottom-right pixel of original should be top-right in rotated");
    }

    @Test
    @DisplayName("Test writeLandscapeImageToModifiedDirectory")
    void shouldWriteLandscapeImagesToModifiedDirectory(@TempDir Path tempDir) throws IOException {
        // Arrange
        var modifiedDirectory = tempDir.resolve("modified");
        Files.createDirectories(modifiedDirectory);
        // Create portrait images in the temp directory
        var portrait1 = createTestImage(modifiedDirectory, "portrait1.png",200, 1000);
        var portrait2 = createTestImage(modifiedDirectory, "portrait2.png",500, 1000);
        var portrait3 = createTestImage(modifiedDirectory, "portrait3.png",600, 1000);

        Set<Path> pathSet = new HashSet<>();
        pathSet.add(portrait1); pathSet.add(portrait2); pathSet.add(portrait3);

        // Act
        assertTrue(Files.exists(modifiedDirectory), "Modified directory should be exist");

        assertAll(
                ()-> assertTrue(Files.exists(portrait1)),
                ()-> assertTrue(Files.exists(portrait2)),
                ()-> assertTrue(Files.exists(portrait3))
        );
    }

    @Test
    @DisplayName("Test writeLandscapeImageToModifiedDirectory: throw RuntimeException")
    void shouldThrowExceptionDuringWrite(@TempDir Path tempDir) throws IOException {
        // arrange
        Path modifiedDirectory = tempDir.resolve("modified");
        Files.createDirectories(modifiedDirectory);

        // create portrait images
        var portrait1 = createTestImage(modifiedDirectory, "portrait1.png",200, 1000);
        Set<Path> pathSet = new HashSet<>();
        pathSet.add(portrait1);

        // Mock Files.list to throw IOException
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(
                    ()-> Files.createDirectories(any(Path.class))
            ).thenThrow(IOException.class);

            // Act & Assert
            assertThrows(
                    RuntimeException.class,
                    ()-> imageProcessor.writeLandscapeImageToModifiedDirectory(pathSet, Path.of(""))
            );
        }
    }

    @Test
    @DisplayName("Test writeLandscapeImageToModifiedDirectory: throw when write")
    void shouldThrowExceptionWhenWrite(@TempDir Path tempDir) throws IOException {
        // arrange
        Path modifiedDirectory = tempDir.resolve("modified");
        Files.createDirectories(modifiedDirectory);

        // create portrait images
        var portrait1 = createTestImage(modifiedDirectory, "portrait1.png",200, 1000);
        Set<Path> pathSet = new HashSet<>();
        pathSet.add(portrait1);
        try (var mockedIO = mockStatic(ImageIO.class)) {
            mockedIO.when(
                    ()-> ImageIO.write(any(BufferedImage.class), anyString(),any(File.class))
            ).thenThrow(IOException.class);

            // Act & Assert
            assertThrows(
                    RuntimeException.class,
                    ()-> imageProcessor.writeLandscapeImageToModifiedDirectory(pathSet, Path.of(""))
            );
        }
    }

}
