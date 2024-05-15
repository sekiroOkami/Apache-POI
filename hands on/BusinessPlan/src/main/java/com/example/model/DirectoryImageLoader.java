package com.example.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryImageLoader {
    private Path directoryPath;
    private List<String> supportedExtensions = List.of("png", "jpg", "jpeg", "gif", "bmp");

    public DirectoryImageLoader(String path) {
        this.directoryPath = Paths.get(path);
    }

    public List<Path> loadImages() throws IOException {
        List<Path> imagePaths = new ArrayList<>();

        // check if the directory exists and is indeed a directory
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            try (Stream<Path> stream = Files.walk(directoryPath)) {
                imagePaths = stream
                        .filter(Files::isRegularFile)
                        .filter(this::hasSupportedExtension)
                        .collect(Collectors.toList());

            }
        } else {
            throw new IOException("The specified path is not a directory or does not exist: " + directoryPath);
        }
        return imagePaths;
    }

    private boolean hasSupportedExtension(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        for (String extension : supportedExtensions) {
            if (fileName.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

}
