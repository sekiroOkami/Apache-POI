package com.example.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExcelImageInserter {
    private XSSFWorkbook wb;
    private Sheet sheet;
    private CreationHelper helper;

    public ExcelImageInserter() {
        this.wb = new XSSFWorkbook();
        String safeSheetName = WorkbookUtil.createSafeSheetName("Sheet1");
        this.sheet = wb.createSheet(safeSheetName);
        this.helper = wb.getCreationHelper();
    }

    public void addImagesToSheet(List<File> imageFiles, int startingRow, int rowHeight) throws Exception {
        ImageProcessor imageProcessor = new ImageProcessor();
        int width = 600;
        int height = 600;

        int currentRow = startingRow;
        for (File imageFile: imageFiles) {
            BufferedImage resizedImage = imageProcessor.resizeImage(imageFile, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            int pictureIndex = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(1);  // column B
            anchor.setRow1(currentRow);
            anchor.setCol2(10); // span across 10 columns
            anchor.setRow2(currentRow + rowHeight);
            Picture picture = drawing.createPicture(anchor, pictureIndex);
            // Optionally resize the picture to fit the anchor's bounds
            // picture.resize(); // Uncomment if you want to resize the picture to fit the anchor
            currentRow += rowHeight;
        }
    }

    public void addImagesToSheet(List<Path> imageFiles, int startingRow, int rowHeight, int picHeight) throws Exception {
        ImageProcessor imageProcessor = new ImageProcessor();
        int currentRow = startingRow;
        for (Path imageFile: imageFiles) {
            BufferedImage img = ImageIO.read(imageFile.toFile());
            BufferedImage resizedImage = imageProcessor.resize(img,picHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            int pictureIndex = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(1);  // column B
            anchor.setRow1(currentRow);
            anchor.setCol2(10); // span across 10 columns
            anchor.setRow2(currentRow + rowHeight);
            Picture picture = drawing.createPicture(anchor, pictureIndex);
            // Optionally resize the picture to fit the anchor's bounds
            // picture.resize(); // Uncomment if you want to resize the picture to fit the anchor
            currentRow += rowHeight;
        }
    }

    public void saveWorkbook(String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            wb.write(fileOut);
        } finally {
            wb.close();
        }
    }
}
