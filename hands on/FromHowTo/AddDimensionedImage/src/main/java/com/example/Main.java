package com.example;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream("AddDimensionedImages.xlsx");
        ) {
            Sheet sheet = wb.createSheet("Picture Test" );
            new AddDimensionedImage().addImageToSheet("B5", sheet, sheet.createDrawingPatriarch(),
                    Paths.get(".\\src\\main\\java\\com\\example\\demoPicture.jpg").toFile().toURI().toURL(), 200, 200,
                    AddDimensionedImage.EXPAND_ROW_AND_COLUMN
            );
            wb.write(fileOut);
        }
    }
}
