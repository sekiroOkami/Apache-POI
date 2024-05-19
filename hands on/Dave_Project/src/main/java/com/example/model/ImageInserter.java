package com.example.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class ImageInserter {
    public static void addImagesToExcel(List<Path> pathList, Path outputFilePath) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(outputFilePath.toFile());
        ) {
            Sheet sheet = workbook.createSheet("Images" );
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();

            int currentRow = 0;
            for (Path imagePath : pathList) {
                InputStream  is = new FileInputStream(imagePath.toFile());
                byte[] bytes = IOUtils.toByteArray(is);
                BufferedImage image = ImageIO.read(imagePath.toFile());
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                is.close();


                int col1 = 1;
                int col2 =col1 + 5; // 5 columns wide
                int row1 = currentRow;
                int row2 = row1 + (image.getHeight() / image.getWidth()) * 5; // Adjust height proportionally

                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(col1);
                anchor.setRow1(row1);
                anchor.setCol2(col2);
                anchor.setRow2(row2);

                drawing.createPicture(anchor, pictureIdx);
                currentRow = row2+1;
            }
            workbook.write(fileOut);
            System.out.println("Excel file with images created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
