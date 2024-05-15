package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class AddImageToExcel {
    public static void main(String[] args) throws Exception {
        var wb = new XSSFWorkbook();
        var sheet = wb.createSheet("Image");
        int width = 600;
        int height = 600;
        String file = new String(".\\src\\main\\resources\\images\\TacoCloud.png");

        try {
            BufferedImage originalImage = ImageIO.read(new File(file));

            // resize the image
            BufferedImage resizedImage = resizeImage(originalImage, width, height);

            // Save the resized img
            File tempFile = File.createTempFile("resizedImg",".png");
            ImageIO.write(resizedImage, "png", tempFile);

            // load an image file
            try (InputStream inputStream = new FileInputStream(tempFile)) {
                // convert the image to a byte array
                byte[] bytes = IOUtils.toByteArray(inputStream);

                // add the image to the workbook
                int pictureIndex = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                // Create the drawing patriarch
                Drawing<?> drawing = sheet.createDrawingPatriarch();

                // create an anchor that is attached to the worksheet
                CreationHelper helper = wb.getCreationHelper();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                // Define the position of the image
                anchor.setCol1(1); // Column B
                anchor.setRow1(1); // Row 2
                anchor.setCol2(5); // Column F
                anchor.setRow2(6); // Row 7

                // insert the picture
                Picture picture = drawing.createPicture(anchor, pictureIndex);

                // Resize the picture to fit the anchor
                picture.resize();
                try (FileOutputStream fileOut = new FileOutputStream("workbook_with_picture.xlsx")) {
                    wb.write(fileOut);
                    System.out.println("Image added to the excel file successfully!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, width, height, null);
        return resizedImage;
    }
}