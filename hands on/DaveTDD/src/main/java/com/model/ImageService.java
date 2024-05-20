package com.model;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;

public interface ImageService {
    public List<Path> loadImages(Path imagesDirectory);
    public List<Path> filterPortraitImages(List<Path> imagePath);
    public boolean isPortrait(Path pathOfImage);
    public BufferedImage rotateImage(BufferedImage image);
    public void writeLandscapeImageToModifiedDirectory(List<Path> portraitList, Path modifiedDirectoryLocation);

}
