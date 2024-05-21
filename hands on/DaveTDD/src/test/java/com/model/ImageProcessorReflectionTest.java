package com.model;

import com.model.ImageProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageProcessorReflectionTest {
    @Test
    @DisplayName("Test isImageFile() using reflection")
    void testIsImageFile() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ImageProcessor imageProcessor = new ImageProcessor();
        Method isImageFileMethod = ImageProcessor.class.getDeclaredMethod("isImageFile", Path.class);
        isImageFileMethod.setAccessible(true);

        var imagePath = Path.of("test.jpg");
        var nonImagePath = Path.of("test.txt");

        var isImage = (boolean) isImageFileMethod.invoke(imageProcessor, imagePath);
        var isNonImage = (boolean) isImageFileMethod.invoke(imageProcessor, nonImagePath);

        assertTrue(isImage);
        assertFalse(isNonImage);
    }
}
