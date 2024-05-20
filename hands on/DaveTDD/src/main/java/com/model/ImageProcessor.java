package com.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageProcessor implements ImageService {
    @Override
    public List<Path> loadImages(Path imagesDirectory)  {
        try (Stream<Path> pathStream = Files.walk(imagesDirectory).parallel()){
            return pathStream.filter(Files::isRegularFile)
                .filter(ImageProcessor::isImageFile)
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isImageFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        for (ImageFileType type : ImageFileType.values()) {
            if (fileName.endsWith(type.getExtension())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Path> filterPortraitImages(List<Path> imagePath) {
        return null;
    }

    @Override
    public boolean isPortrait(Path pathOfImage) {
        return false;
    }

    @Override
    public BufferedImage rotateImage(BufferedImage image) {
        return null;
    }

    @Override
    public void writeLandscapeImageToModifiedDirectory(List<Path> portraitList, Path modifiedDirectoryLocation) {

    }
}
