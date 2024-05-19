package com.example.model;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String imageDirectoryPath = ".\\src\\main\\resources\\\\images";

        // Initialize the DirectoryImageLoader
        DirectoryImageLoader imageLoader = new DirectoryImageLoader(imageDirectoryPath);

        // Initialize the ExcelImageInserter
        ExcelImageInserter imageInserter = new ExcelImageInserter();

        try {
            // Load images from the specified directory
            List<Path> imageFiles = imageLoader.loadImages();
            // Initialize the ImageOrientationConverter
            ImageOrientationConverter converter = new ImageOrientationConverter();
            converter.convertToLandscape(imageFiles);

            imageInserter.addImagesToSheet(imageFiles, 2, 10,200);
            imageInserter.saveWorkbook("output.xlsx");
            System.out.println("File created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
