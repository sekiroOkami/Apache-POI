package com.example.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ImageOrientationConverter {
    private Path outputDirectory;


    public void convertToLandscape(List<Path> imagePaths) throws IOException {
        imagePaths.stream()
                .filter(this::isPortriat)
                .forEach(this::convertAndSave);
    }

    private void convertAndSave(Path imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(imagePath.toFile());
            BufferedImage landscapeImage = rotateImage(originalImage);

            // save the landsccape image to the new directory
//            Path outputFilePath = outputDirectory.resolve(imagePath.getFileName());
            ImageIO.write(landscapeImage, "png", imagePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage rotateImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics = rotatedImage.createGraphics();
        graphics.translate((width - height) / 2, (width - height) / 2);
        graphics.rotate(Math.PI / 2, width / 2, height / 2);
        graphics.drawRenderedImage(originalImage, null);
        graphics.dispose();
        return rotatedImage;
    }

    private boolean isPortriat(Path imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(imagePath.toFile());
            return originalImage.getWidth() < originalImage.getHeight();
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
