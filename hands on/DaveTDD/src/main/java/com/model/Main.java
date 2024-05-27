package com.model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        ImageProcessor imageProcessor = new ImageProcessor();
        Path imageDirectory = Paths.get(".\\src\\test\\resources\\images");
        imageProcessor.writeLandscapeImageToModifiedDirectoryIfImageIsPortraitRotateBeforeWrite(imageDirectory,
                Path.of("modified"));
    }
}
