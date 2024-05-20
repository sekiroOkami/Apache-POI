package com.model.demo;

import com.model.ImageProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TempDirDemo {
    @Test
    void multipleTemDirsTest(@TempDir Path tempDir1, @TempDir Path tempDir2) throws IOException {
        File newFile1 = new File(tempDir1.toFile(), "example1.txt");
        File newFile2 = new File(tempDir2.toFile(), "example2.txt");
        assertTrue(newFile1.createNewFile());
        assertTrue(newFile2.createNewFile());
    }

    @Test
    void shouldReturnListOfImages(@TempDir Path tempDir) throws IOException {
        List<Path> pathList = new ArrayList<>();
        // Create some image files in the temp directory
        Path image1Path = tempDir.resolve("image1.jpg");
        Files.createFile(image1Path);
        Path image2Path = tempDir.resolve("image2.png");
        Files.createFile(image2Path);
        Path image3Path = tempDir.resolve("image3.jpeg");
        Files.createFile(image3Path);
        // Create a non-image file
        Files.createFile(tempDir.resolve("document.txt"));
        pathList.add(image1Path);
        pathList.add(image2Path);
        pathList.add(image3Path);
        // Check if the files exist in the temporary directory
        assertTrue(Files.exists(image1Path));
        assertTrue(Files.exists(image2Path));
        assertTrue(Files.exists(image3Path));
        assertEquals(3, pathList.size());
        // Additional assertions if needed
    }

    public static void main(String[] args) {
        List<Path> paths = new ImageProcessor().loadImages(Paths.get(".\\src\\test\\resources\\images"));
        System.out.println(paths.size());
    }
}
