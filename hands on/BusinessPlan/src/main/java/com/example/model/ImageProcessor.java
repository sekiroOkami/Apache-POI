package com.example.model;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

// responsible for resizing images
public class ImageProcessor {
    public BufferedImage resizeImage(File imageFile, int width, int height) throws Exception {
        BufferedImage orinalImgae = ImageIO.read(imageFile);
        BufferedImage resizedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(
                orinalImgae, 0, 0, width, height, null
        );
        return resizedImage;
    }

    public BufferedImage resize(BufferedImage img, int newHeight) {
        System.out.println("Scaling image");
        double scaleFactor = (double) newHeight /img.getHeight();
        BufferedImage scaledImg = new BufferedImage(
                (int) (scaleFactor*img.getWidth()), newHeight, BufferedImage.TYPE_INT_ARGB
        );
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(
                at, AffineTransformOp.TYPE_BILINEAR
        );
        return scaleOp.filter(img, scaledImg);
    }
}
