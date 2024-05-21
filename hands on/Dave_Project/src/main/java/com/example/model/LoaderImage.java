package com.example.model;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoaderImage {
    public static List<Path> loadImages(Path directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(directoryPath, FileVisitOption.FOLLOW_LINKS).parallel()) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
                    })
                    .collect(Collectors.toList());
        }
    }
}
