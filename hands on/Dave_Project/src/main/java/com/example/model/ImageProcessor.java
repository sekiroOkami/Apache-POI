package com.example.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageProcessor {

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
    public static List<Path> filterPortraitImage(List<Path> imagePaths) throws IOException {
        return
        imagePaths.parallelStream()
                .filter(ImageProcessor::isPortrait)
                .collect(Collectors.toList());
    }

    private static boolean isPortrait(Path imagePath) {
        try {
            BufferedImage image = ImageIO.read(imagePath.toFile());
            return image.getWidth() < image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void writeLandscapeImageToModifiedDirectory(List<Path> portraitList, Path modifiedDirectory) {
        portraitList.parallelStream()
                .forEach(portraitPath -> {
                    try {
                        BufferedImage originalImage = ImageIO.read(portraitPath.toFile());
                        BufferedImage modifiedImage = rotateImage(originalImage);
                        // extract the filename
                        String fileName = portraitPath.getFileName().toString();
                        // construct the output path
                        Path outputPath = modifiedDirectory.resolve(fileName);
                        // Ensure the modified directory exists
                        if (!Files.exists(outputPath)) {
                            Files.createDirectories(outputPath);
                        }
                        ImageIO.write(modifiedImage, "png", outputPath.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static BufferedImage rotateImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, originalImage.getType());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedImage.setRGB(y, x, originalImage.getRGB(x, y));
            }
        }
        return rotatedImage;
    }
}
