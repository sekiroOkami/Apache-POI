package com.model.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public sealed interface ImageService permits ImageProcessor {
    public Set<Path> loadImages(Path... imageDirectory);

    public Path writeImageToModifiedDirectory(Path imagesDirectory, Path modifiedDirectory);
    public boolean isPortrait(Path pathOfImage) throws IOException;
    public BufferedImage rotateImage(BufferedImage image);
}
