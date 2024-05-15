package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkingWithPictures {

    public static void main(String[] args) throws IOException {

        // create a new workbook
        try (var wb = new XSSFWorkbook()) {
            CreationHelper helper = wb.getCreationHelper();

            // add a picture in this workbook
            Path img = Paths.get(".\\src\\main\\resources\\images\\TacoCloud.png");
            InputStream is = new FileInputStream(img.toFile());
            byte[] bytes = IOUtils.toByteArray(is);
            is.close();
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

            // create sheet
            Sheet sheet = wb.createSheet();

            // create drawing
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            // Define the position of the image
            anchor.setCol1(1); // Column B
            anchor.setRow1(1); // Row 2
            anchor.setCol2(5); // Column F
            anchor.setRow2(6); // Row 7
            Picture picture = drawing.createPicture(anchor, pictureIdx);

            // auto-size picture
            picture.resize(0.5,0.5);

            // save workbook
            String file = "picture.xlsx";
            try(OutputStream os = new FileOutputStream(file)) {
                wb.write(os);
            }
        }
    }
}
