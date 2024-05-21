package com.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ImageService {
    public Set<Path> loadImages(Path imagesDirectory);
    public Set<Path> filterPortraitImages(Set<Path> imagePath) throws IOException;
    public boolean isPortrait(Path pathOfImage) throws IOException;
    public BufferedImage rotateImage(BufferedImage image);
    public void writeLandscapeImageToModifiedDirectory(Set<Path> portraitList, Path modifiedDirectoryLocation);

}
