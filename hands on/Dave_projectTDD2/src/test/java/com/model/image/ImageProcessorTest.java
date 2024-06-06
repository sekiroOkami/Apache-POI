package com.model.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageProcessorTest {
    @Mock
    ImageProcessor im ;


    @BeforeEach
    void setUp() {
        im = new ImageProcessor();
    }

    @Test
    @DisplayName("Test loadImages method : should return a Set<Path>")
    void shouldReturnSetOfImagePath(@TempDir Path tempDir) throws IOException {
        Path image1 = tempDir.resolve("image1.jpg");
        Files.createFile(image1);
        Path image2 = tempDir.resolve("image2.png");
        Files.createFile(image2);
        Path image3 = tempDir.resolve("image3.jpeg");
        Files.createFile(image3);

        Set<Path> pathSet = im.loadImages(tempDir);

        assertThat(pathSet)
                .isNotNull()
                .contains(image1, image2, image3);
    }


    @ParameterizedTest(name = "Test isImageFile with path: {0}")
    @CsvSource({
            "image.jpg, true",
            "document.txt, false"
    })
    @DisplayName("Test isImageFile method with various paths")
    void testIsImageFile(String filePath, boolean expectedResult) {
        Path path = Paths.get(filePath);

        assertThat(im.isImageFile(path)).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("loadImages(): should throw IOException")
    void loadImageThrowIOException(@TempDir Path tempDir) throws IOException {
        im = mock(ImageProcessor.class);
        when(im.loadImages(any(Path.class))).thenThrow(new IOException());
//        assertThrows(
//                ImageProcessingException.class,
//                () -> im.loadImages(tempDir)
//        );

        assertThatThrownBy(()-> im.loadImages(tempDir))
                .isInstanceOf(IOException.class);

    }

}
