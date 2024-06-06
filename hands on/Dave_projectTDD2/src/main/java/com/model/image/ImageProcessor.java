package com.model.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ImageProcessor implements ImageService {
    @Override
    public Set<Path> loadImages(Path imagesDirectories) throws IOException {
        Objects.requireNonNull(imagesDirectories, "Directory paths must not be null");
        Stream<Path> pathStream = Files.list(imagesDirectories);
            return new HashSet<>(pathStream.filter(path ->
                    Files.isRegularFile(path) && this.isImageFile(path)).collect(Collectors.toSet()));
    }

    public boolean isImageFile(Path path) {
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
    public Path writeImageToModifiedDirectory(Path imagesDirectory, Path modifiedDirectoryLocation) {
        // Ensure the modified directory exits
        if (!Files.exists(modifiedDirectoryLocation)) {
            try {
                Files.createDirectories(modifiedDirectoryLocation);
                // extract the filename
                String fileName = imagesDirectory.getFileName().toString();
                BufferedImage image = ImageIO.read(imagesDirectory.toFile());
                // construct the output path
                Path destinationPath = modifiedDirectoryLocation.resolve(fileName);
                ImageIO.write(image, String.valueOf(ImageFileType.JPEG), destinationPath.toFile());
                return destinationPath;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public boolean isPortrait(Path pathOfImage) throws IOException {
        BufferedImage image = ImageIO.read(pathOfImage.toFile());
        return image.getWidth() < image.getHeight();
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


    public Set<Path> collectLandscapeImages(Path directories) throws IOException {
        Set<Path> allImages  = loadImages(directories);

        // Filter out only landscape images
        return allImages.stream()
                .filter(path -> {
                    try {
                        return !isPortrait(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Error checking image orientation: " + path, e);
                    }
                })
                .collect(Collectors.toSet());
    }
}
