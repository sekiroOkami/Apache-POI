package com.model;

import com.model.exception.ImageProcessingException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ImageProcessor implements ImageService {
    @Override
    public Set<Path> loadImages(Path imagesDirectory)  {
        Objects.requireNonNull(imagesDirectory);
        try (Stream<Path> pathStream = Files.list(imagesDirectory).parallel()){
            return pathStream.filter(Files::isRegularFile)
                .filter(ImageProcessor::isImageFile)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to read image file: " + imagesDirectory, e);
        }
    }

    private static boolean isImageFile(Path path) {
        Objects.requireNonNull(path);
        String fileName = path.getFileName().toString().toLowerCase();
        for (ImageFileType type : ImageFileType.values()) {
            if (fileName.endsWith(type.getExtension())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Path> filterPortraitImages(Set<Path> imagePath) {
        Objects.requireNonNull(imagePath);
        return
        imagePath.parallelStream()
                .filter(this::isPortrait)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isPortrait(Path imagePath)  {
        Objects.requireNonNull(imagePath);
        try {
            BufferedImage image = ImageIO.read(imagePath.toFile());
            return image.getWidth() < image.getHeight();
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to read image file: " + imagePath, e);
        }
    }

    @Override
    public void writeLandscapeImageToModifiedDirectory(Set<Path> portraitPath, Path modifiedDirectoryLocation) {
        Objects.requireNonNull(modifiedDirectoryLocation);
        Objects.requireNonNull(portraitPath);

        portraitPath.parallelStream()
            .forEach(path -> {
                try {
                    BufferedImage portraitImage = ImageIO.read(path.toFile());
                    BufferedImage landscapeImage = rotateImage(portraitImage);
                    writeImagePathToDirectoryLocation(modifiedDirectoryLocation, path, landscapeImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private static void writeImagePathToDirectoryLocation(Path modifiedDirectoryLocation, Path path, BufferedImage landscapeImage) throws IOException {
        // extract the filename
        String fileName = path.getFileName().toString();
        // construct the output path
        Path destinationPath = modifiedDirectoryLocation.resolve(fileName);
        // Ensure the modified directory exits
        if (!Files.exists(modifiedDirectoryLocation)) {
            Files.createDirectories(modifiedDirectoryLocation);
        }
        ImageIO.write(landscapeImage, String.valueOf(ImageFileType.JPEG), destinationPath.toFile());
    }

    @Override
    public BufferedImage rotateImage(BufferedImage originalImage) {
        Objects.requireNonNull(originalImage);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, originalImage.getType());
        // rotate counterclockwise
        IntStream.range(0, height)
                .parallel().forEach(y-> {
                    IntStream.range(0, width).forEach(x-> {
                        rotatedImage.setRGB(y, x, originalImage.getRGB(x, y));
                    });
                });
        return rotatedImage;
    }


    public void writeLandscapeImageToModifiedDirectoryIfImageIsPortraitRotateBeforeWrite(Path imagesDirectory,
                                                                                           Path modifiedDirectoryLocation) throws IOException {
        Objects.requireNonNull(imagesDirectory);
        // load all of images
        Set<Path> pathSetOfImage = loadImages(imagesDirectory);
        // Process each image in parallel
        pathSetOfImage.parallelStream()
                .forEach(path -> {
                    try {
                        BufferedImage target;
                        if (isPortrait(path)) {
                            target = rotateImage(ImageIO.read(path.toFile()));
                        } else {
                            target = ImageIO.read(path.toFile());
                        }
                        writeImagePathToDirectoryLocation(modifiedDirectoryLocation, path, target);
                    } catch (IOException e) {
                        throw new RuntimeException("Error processing image: " + path, e);
                    }
                });
    }
}
