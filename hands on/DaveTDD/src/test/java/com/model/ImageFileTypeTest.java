package com.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageFileTypeTest {

    @Test
    void testImageFileTypeExtensions() {
        assertEquals(".png", ImageFileType.PNG.getExtension());
        assertEquals(".jpg", ImageFileType.JPG.getExtension());
        assertEquals(".jpeg", ImageFileType.JPEG.getExtension());
        assertEquals(".gif", ImageFileType.GIF.getExtension());
    }

    @Test
    void testImageFileTypeValues() {
        ImageFileType[] expectedValues = {ImageFileType.PNG, ImageFileType.JPG, ImageFileType.JPEG, ImageFileType.GIF};
        assertArrayEquals(expectedValues, ImageFileType.values());
    }

    @Test
    public void testValueOf() {
        assertEquals(ImageFileType.PNG, ImageFileType.valueOf("PNG"));
        assertEquals(ImageFileType.JPG, ImageFileType.valueOf("JPG"));
        assertEquals(ImageFileType.JPEG, ImageFileType.valueOf("JPEG"));
        assertEquals(ImageFileType.GIF, ImageFileType.valueOf("GIF"));
    }

}