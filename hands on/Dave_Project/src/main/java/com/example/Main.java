package com.example;

import com.example.model.ImageProcessor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.model.ImageInserter.addImagesToExcel;
import static com.example.model.ImageProcessor.*;

public class Main {
    public static final int EMU_PER_INCH = 914400;
    public static void main(String[] args) throws Exception {
        Path imagesDirectory = Paths.get(".\\src\\main\\resources\\images");
//        Path outputDirectory = Paths.get(".\\src\\main\\resources\\portraitPic\\modifiedImage");
        List<Path> pathList = loadImages(imagesDirectory);
        List<Path> portraitImage = filterPortraitImage(pathList);
        System.out.println(portraitImage);
    }

    private static void demo1() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Images");

        // Load the image
        InputStream is = new FileInputStream("E:\\Java\\dave\\Dave_Project\\src\\main\\resources\\images\\IMG_1430.JPG");
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        is.close();

        // Create the drawing patriarch
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        // Calculate the dimensions in EMUs
        int col1 = 1; // Column B
        int row1 = 1; // Row 2
        int widthInInches = 2;
        int heightInInches = 3;
        int widthInEmu = widthInInches * EMU_PER_INCH;
        int heightInEmu = heightInInches * EMU_PER_INCH;

        // Create an anchor with the image's dimensions and position
        ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, col1, row1, col1 + 1, row1 + 1);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

        // Add the image to the sheet
        drawing.createPicture(anchor, pictureIdx);

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("images.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
    }
}
